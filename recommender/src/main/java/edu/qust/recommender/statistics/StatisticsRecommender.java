package edu.qust.recommender.statistics;

//历史热门表  RateMoreProducts
//近期热门统计 RateMoreRecentlyProducts
//每个商品的平均评分表  AverageScoreProducts


import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.recommender.dataLoader.ProductRating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class StatisticsRecommender {


    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private ProductRatingServiceApi productRatingServiceApi;

    public void compute(){
        List<ProductRating> ratingList = productRatingServiceApi.list().stream().map(
                e -> BeanUtil.map(e, ProductRating.class)
        ).collect(Collectors.toList());
        Dataset<Row> ratingDF = sparkSession.createDataFrame(ratingList,ProductRating.class);
        //创建ratings临时表
        ratingDF.createOrReplaceTempView("ratings");
        ratingDF.cache();
        //1. 历史热门商品， 按照评分个数统计
        Dataset<Row> rateMoreProductsDF = sparkSession.sql(
                "select productId, count(productId) as count " +
                        "from ratings group by productId order by count desc");
        rateMoreProductsDF.cache();
        // 写入表 redis 代替
        rateMoreProductsDF.show();
        List<Integer> hotProductId = new ArrayList<>();
        rateMoreProductsDF.collectAsList().forEach( e -> {
            Integer i = e.getInt(0);
            hotProductId.add(i);
        });
        redisTemplate.opsForList().leftPushAll("historicalHot",hotProductId);
        //2. 近期热门商品统计。 时间戳转换为yyMM格式进行评分统计
        //自定义函数  //入参为Integer
        sparkSession.udf().register("changeDate",new changeDateUDF(), DataTypes.StringType);

        Dataset<Row> ratingOfYearMonDF = sparkSession.sql(
                "select productId, score, changeDate(timestamp) as yearmonth from ratings");
        //创建临时表
        ratingOfYearMonDF.createOrReplaceTempView("ratingOfMonth");
        Dataset<Row> rateMoreRecentlyProductsDF = sparkSession.sql(
                "select productId, count(productId) as count, yearmonth " +
                        "from ratingOfMonth group by yearmonth, productId " +
                        "order by yearmonth desc, count desc");
        //TODO 把DF写入mysql
        /*List<Integer> recentlyHotProductId =rateMoreRecentlyProductsDF.toJavaRDD().map( e -> e.getInt(1)).collect();
        redisTemplate.opsForList().leftPushAll("historialHot",);*/

        //3. 优质商品统计，商品的平均评分
        Dataset<Row> averageProductsDF = sparkSession.sql(
                "select productId, avg(score) as avg " +
                        "from ratings group by productId order by avg desc");
        //TODO  写入数据库

        List<Integer> highRatingProductId = new ArrayList<>();
                averageProductsDF.collectAsList().forEach(
                        e -> highRatingProductId.add(e.getInt(0)));
        redisTemplate.opsForList().leftPushAll("highRating",highRatingProductId);
        //sparkSession.stop();
    }
}
