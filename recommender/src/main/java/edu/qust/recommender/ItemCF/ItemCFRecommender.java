package edu.qust.recommender.ItemCF;


import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.commonInterface.ProductRecsServiceApi;
import edu.qust.commonInterface.param.ProductRecommendationParam;
import edu.qust.commonInterface.param.ProductRecsParam;
import edu.qust.recommender.common.ProductRecs;
import edu.qust.recommender.common.Recommendation;
import edu.qust.recommender.dataLoader.ProductRating;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Component
public class ItemCFRecommender implements Serializable {

    private int MAX_RECOMMENDATION = 10;
    @Autowired

    private SparkSession sparkSession;

    @Reference
    private ProductRecsServiceApi productRecsServiceApi;

    @Reference
    private ProductRatingServiceApi productRatingServiceApi;

    public void recommender(){

        List<ProductRating> ratings = productRatingServiceApi.list().stream().map(
                e -> BeanUtil.map(e, ProductRating.class)
        ).collect(Collectors.toList());

        Dataset<Row> productRatingDataset = sparkSession.createDataFrame(ratings,ProductRating.class);
        //TODO
        System.out.println(productRatingDataset.columns());//"userId", "productId", "score"
        productRatingDataset.printSchema();
        Dataset<Row> ratingDF = productRatingDataset.select("userId","productId","score")
                .toDF("userId", "productId", "score").cache();
        //TODO Core algorithm: calculate co-occurrence similarity between products
        Dataset<Row> productRatingCountDF = ratingDF.groupBy("productId").count();// *productId | count *
        Dataset<Row> ratingWithCountDF = ratingDF.join(productRatingCountDF, "productId");
        Dataset<Row> joinedDF = ratingWithCountDF.join(ratingWithCountDF, "userId")// userId  productId count productId productId count
                .toDF("userId", "product1", "score1", "count1", "product2", "score2", "count2")
                .select("userId", "product1", "count1", "product2", "count2");
        //Create a temporary view for the query
        joinedDF.createOrReplaceTempView("joined");

        //Group by product1 and product2 and count users who rated both products
        Dataset<Row> cooccurrenceDF = sparkSession.sql("select  product1, product2, count(userId) as cocount, first(count1) as count1, first(count2) as count2 " +
                "from joined group by product1, product2").cache();

        //product1,product2,score
        List<ProductRecs> df = new ArrayList<>();
        cooccurrenceDF.collectAsList().stream().map(row -> {
            double coocSim = cooccurrenceSim(row.getAs("cocount"), row.getAs("cocount"), row.getAs("count2"));
            return new Tuple2<>(row.getInt(0), new Tuple2<>(row.getInt(1), coocSim));
        }).collect(Collectors.groupingBy(e -> e._1))
                .forEach( (k,v) -> {
                    List<Recommendation> recs = new ArrayList<>();
                    v.forEach( e-> recs.add(new Recommendation(e._2._1,e._2._2)));
                    recs.stream().filter(e -> e.getProductId() != k)
                            .sorted(Comparator.comparingDouble(Recommendation::getScore).reversed())
                            .limit(MAX_RECOMMENDATION);
                    df.add(new ProductRecs(k,recs));
                });

/*        JavaRDD<ProductRecs> df = cooccurrenceDF.mapToPair(row -> {
                    double coocSim = cooccurrenceSim(row.getAs("cocount"), row.getAs("cocount"), row.getAs("count2"));
                    return new Tuple2<>(row.getInt(0), new Tuple2<>(row.getInt(1), coocSim));
                }).groupByKey()
                .map(item -> {
                    List<Recommendation> recs = new ArrayList<>();
                    item._2.forEach(e -> recs.add(new Recommendation(e._1, e._2)));
                    recs.stream()
                            .filter(e -> e.getProductId() != item._1)
                            .sorted(Comparator.comparingDouble(Recommendation::getScore).reversed())   // Exclude the product itself
                            .limit(MAX_RECOMMENDATION);
                    return new ProductRecs(item._1, recs);
                });*/
        //Dataset<Row> simDF = sparkSession.createDataFrame(df, ProductRecs.class);
        List<ProductRecsParam> productRecsParam = df.stream().map(e -> {
            ProductRecsParam rec = new ProductRecsParam();
            rec.setProductId(e.getProductId());
            List<ProductRecommendationParam> productRecommendationParams = new ArrayList<>();
            e.getRecs().forEach( r ->
                    productRecommendationParams.add(BeanUtil.map(r,ProductRecommendationParam.class)));
            rec.setRecs(productRecommendationParams);
            return rec;
        }).collect(Collectors.toList());
        productRecsServiceApi.save(productRecsParam);
        log.info("success !");
    }

    private double cooccurrenceSim(Long coCount, Long count1, Long count2){
        return coCount / Math.sqrt(count1 * count2);
    }
}
