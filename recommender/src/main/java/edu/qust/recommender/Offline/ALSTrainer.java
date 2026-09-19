package edu.qust.recommender.Offline;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.recommender.dataLoader.ProductRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ALSTrainer {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private JavaSparkContext javaSparkContext;
    @Reference
    private ProductRatingServiceApi productRatingServiceApi;
    public void test(){
        //数据库加载数据
        List<ProductRating> ratings = productRatingServiceApi.list().stream().map(
                e -> BeanUtil.map(e, ProductRating.class)
        ).collect(Collectors.toList());
        JavaRDD<ProductRating> ratingRDD = javaSparkContext.parallelize(ratings);
        ratingRDD.cache();
        JavaRDD<Rating> data = ratingRDD.map(x ->{
            return new Rating(x.getUserId(),x.getProductId(),x.getScore());
        });
        data.cache();
        JavaRDD<Rating>[] splits = data.randomSplit(new double[]{0.8, 0.2});
        JavaRDD<Rating> trainRDD = splits[0];
        JavaRDD<Rating> testingRDD = splits[1];

        adjustALSParams(trainRDD,testingRDD);
    }

    public void adjustALSParams(JavaRDD<Rating> trainRDD, JavaRDD<Rating> testingRDD){
        @Data
        @AllArgsConstructor
        class Parameter{
            int rank;
            double lambads;
            double rmse;
        }
        int[] ranks  ={1,3,5,8,10};
        double[] lambads = {3,1,0.3, 0.1, 0.03, 0.01};
        List<Parameter> rmse = new ArrayList<>();
        for(int rank: ranks){
            for(double lambad: lambads){
                MatrixFactorizationModel model = ALS.train(trainRDD.rdd(), rank,10, lambad);
                rmse.add(new Parameter(rank,lambad,getRMSE(model,testingRDD)));
            }
        }
        rmse.stream().sorted(Comparator.comparing(Parameter::getRmse))
                .forEach(System.out::println);
    }

    public double getRMSE(MatrixFactorizationModel model, JavaRDD<Rating> testingRDD){
        JavaRDD<Tuple2<Integer, Integer>> userProducts =
                testingRDD.map(item -> new Tuple2<>(item.user(), item.product()));
        JavaRDD<Rating> predictRating =  model.predict(JavaPairRDD.fromJavaRDD(userProducts));

        predictRating.cache();
        JavaRDD<Tuple2<Tuple2<Integer, Integer>, Double>> realData = testingRDD.
                map(item -> new Tuple2<>(new Tuple2<>(item.user(), item.product()), item.rating()));
        JavaRDD<Tuple2<Tuple2<Integer, Integer>, Double>> predictData = predictRating.
                map(item -> new Tuple2<>(new Tuple2<>(item.user(), item.product()), item.rating()));
        predictData.cache();
        //实际评分表与预测评分表连接
        Double errs = JavaPairRDD.fromJavaRDD(realData).join(JavaPairRDD.fromJavaRDD(predictData)).mapToDouble(
                item -> {
                    double err = item._2._1 - item._2._2;
                    return err * err;
                }
        ).mean();
        return Math.sqrt(errs);
    }
}
