package edu.qust.recommender.statistics;

//Historical popularity table  RateMoreProducts
//Recent popularity statistics RateMoreRecentlyProducts
//Average product rating table  AverageScoreProducts


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
        //Create the ratings temporary view
        ratingDF.createOrReplaceTempView("ratings");
        ratingDF.cache();
        //1. Historical popularity ordered by rating count
        Dataset<Row> rateMoreProductsDF = sparkSession.sql(
                "select productId, count(productId) as count " +
                        "from ratings group by productId order by count desc");
        rateMoreProductsDF.cache();
        // Write to Redis instead of a database table
        rateMoreProductsDF.show();
        List<Integer> hotProductId = new ArrayList<>();
        rateMoreProductsDF.collectAsList().forEach( e -> {
            Integer i = e.getInt(0);
            hotProductId.add(i);
        });
        redisTemplate.opsForList().leftPushAll("historicalHot",hotProductId);
        //2. Calculate recent popularity by converting timestamps to year-month values
        //Custom function that accepts an Integer
        sparkSession.udf().register("changeDate",new changeDateUDF(), DataTypes.StringType);

        Dataset<Row> ratingOfYearMonDF = sparkSession.sql(
                "select productId, score, changeDate(timestamp) as yearmonth from ratings");
        //Create a temporary view
        ratingOfYearMonDF.createOrReplaceTempView("ratingOfMonth");
        Dataset<Row> rateMoreRecentlyProductsDF = sparkSession.sql(
                "select productId, count(productId) as count, yearmonth " +
                        "from ratingOfMonth group by yearmonth, productId " +
                        "order by yearmonth desc, count desc");
        //TODO Write the DataFrame to MySQL
        /*List<Integer> recentlyHotProductId =rateMoreRecentlyProductsDF.toJavaRDD().map( e -> e.getInt(1)).collect();
        redisTemplate.opsForList().leftPushAll("historialHot",);*/

        //3. Rank high-quality products by average rating
        Dataset<Row> averageProductsDF = sparkSession.sql(
                "select productId, avg(score) as avg " +
                        "from ratings group by productId order by avg desc");
        //TODO  Write to the database

        List<Integer> highRatingProductId = new ArrayList<>();
                averageProductsDF.collectAsList().forEach(
                        e -> highRatingProductId.add(e.getInt(0)));
        redisTemplate.opsForList().leftPushAll("highRating",highRatingProductId);
        //sparkSession.stop();
    }
}
