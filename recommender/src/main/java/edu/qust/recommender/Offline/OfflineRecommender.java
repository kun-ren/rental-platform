package edu.qust.recommender.Offline;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.commonInterface.ProductRecsServiceApi;
import edu.qust.commonInterface.UserRecsServiceApi;
import edu.qust.commonInterface.param.ProductRecommendationParam;
import edu.qust.commonInterface.param.ProductRecsParam;
import edu.qust.commonInterface.param.UserRecommendationParam;
import edu.qust.commonInterface.param.UserRecsParam;
import edu.qust.recommender.common.ProductRecs;
import edu.qust.recommender.common.Recommendation;
import edu.qust.recommender.common.UserRecs;
import edu.qust.recommender.dataLoader.ProductRating;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.*;
import org.jblas.DoubleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OfflineRecommender implements Serializable {


    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private JavaSparkContext javaSparkContext;

    @Reference
    private ProductRatingServiceApi productRatingServiceApi;

    @Reference
    private UserRecsServiceApi userRecsServiceApi;

    @Reference
    private ProductRecsServiceApi productRecsServiceApi;

    String USER_RECS = "UserRecs";
    String PRODUCT_RECS = "ProductRecs";
    int USER_MAX_RECOMMENDATION = 20;
    public void recommend(){
        List<ProductRating> ratings = productRatingServiceApi.list().stream().map(
                e -> BeanUtil.map(e, ProductRating.class)
        ).collect(Collectors.toList());
        log.info(String.valueOf(ratings.size()));
        JavaRDD<ProductRating> ratingRDD = javaSparkContext.parallelize(ratings);
        ratingRDD.cache();
        //Extract the distinct user and product data sets
        JavaRDD<Integer> userRDD = ratingRDD.map( item -> {
            return item.getUserId();
        }).distinct();
        userRDD.cache();

        JavaRDD<Integer> productRDD = ratingRDD.map(item -> {
            return item.getProductId();
        }).distinct();
        productRDD.cache();
        //1. Latent-factor model

        JavaRDD<Rating> trainData = ratingRDD
                .map(x -> new Rating(x.getUserId(),x.getProductId(),x.getScore()));
        trainData.cache();
        int rank = 5;
        int iterations = 10;
        double lambda = 0.01;
        MatrixFactorizationModel model = ALS.train(trainData.rdd(),rank,iterations, lambda );
        //2. Generate the predicted rating matrix
        //Create candidate user-product pairs with the Cartesian product of userRDD and productRDD
        JavaPairRDD<Integer, Integer> userProduct = userRDD.cartesian(productRDD);
        userProduct.cache();
        JavaRDD<Rating> preRating = model.predict(userProduct);
        Map<Integer, List<Rating>> map = preRating.collect().stream().filter(item -> item.rating() > 0)
                .collect(Collectors.groupingBy(Rating::user));

        List<UserRecs> userRecsList = new ArrayList<>();
        map.forEach( (k,v) ->{
            UserRecs rec = new UserRecs();
            rec.setUserId(k);
            List<Recommendation> recommendations = new ArrayList<>();
            v.forEach( e -> {
                Recommendation r = new Recommendation();
                r.setProductId(e.product());
                r.setScore(e.rating());
                recommendations.add(r);
            });
            List<Recommendation> sorted = recommendations.stream()
                    .sorted(Comparator.comparing(Recommendation::getScore)
                            .reversed()).limit(USER_MAX_RECOMMENDATION)
                    .collect(Collectors.toList());
            rec.setRecs(sorted);
            userRecsList.add(rec);
            });
        //Dataset<Row> userRecs = sparkSession.createDataFrame(userRecsList,UserRecs.class);
        //userRecs.cache();
        //userRecs.show();// print
        log.info(String.valueOf(userRecsList.size()));
        List<UserRecsParam> userRecsParams = new ArrayList<>();
        userRecsList.forEach(e ->{
            UserRecsParam recs = new UserRecsParam();
            recs.setUserId(e.getUserId());
            List<UserRecommendationParam> userRecommendationParams =new ArrayList<>();
            e.getRecs().forEach(t ->
                    userRecommendationParams.add(BeanUtil.map(t, UserRecommendationParam.class)));

            recs.setRecs(userRecommendationParams);
            userRecsParams.add(recs);
        });
        log.info(String.valueOf(userRecsParams.size()));
        userRecsServiceApi.save(userRecsParams);

        //3.Calculate product similarity from product feature vectors
        JavaRDD<Tuple2<Integer, DoubleMatrix>> productFeatures = model.productFeatures().toJavaRDD().map(
                item ->new Tuple2<>((Integer)item._1, new DoubleMatrix(item._2)));
        //Calculate cosine similarity between products
        JavaRDD<ProductRecs> productRecsRDD
                = productFeatures.cartesian(productFeatures)
                .filter( item -> item._1._1 != item._2._1)// Remove Cartesian-product pairs with the same product ID
                .mapToPair(
                        item ->{
                            double similarity = consinSim(item._1._2, item._2._2);
                             return new Tuple2<>(item._1._1, new Tuple2<>(item._2._1, similarity));// Return (productID, relatedProductId, similarity)
                        }
                )
                .filter(item -> item._2._2 > 0.4)  //Keep products with similarity greater than 0.4
                .groupByKey()
                .map(item -> {
                    List<Recommendation> recs = new ArrayList<>();
                    item._2.forEach( e -> recs.add(new Recommendation(e._1, e._2)));
                    return new ProductRecs(item._1, recs);
                });
        Dataset<Row> productRecs = sparkSession.createDataFrame(productRecsRDD,ProductRecs.class);
        List<ProductRecsParam> productRecsParam = productRecs.collectAsList().stream().map(e -> {
            ProductRecsParam rec = new ProductRecsParam();
            rec.setProductId(e.getInt(1));
            List<ProductRecommendationParam> productRecommendationParams = new ArrayList<>();
            e.getList(2).forEach( r ->
                    productRecommendationParams.add(BeanUtil.map((Recommendation)r,ProductRecommendationParam.class)));
            rec.setRecs(productRecommendationParams);
            return rec;
        }).collect(Collectors.toList());
        System.out.println(productRecsParam.size());
        productRecsServiceApi.save(productRecsParam);

    }

    private double consinSim(DoubleMatrix a, DoubleMatrix  b){
        double similarity = a.dot(b) / ( a.norm2() * b.norm2() );
        return similarity;
    }
}
