package edu.qust.recommender.dataLoader;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.commonInterface.StuffServiceApi;
import edu.qust.commonInterface.param.ProductRatingParam;
import edu.qust.commonInterface.param.StuffApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataLoader {

    @Autowired
    private JavaSparkContext javaSparkContext;
    @Autowired
    private SparkSession sparkSession;
    @Reference
    private ProductRatingServiceApi productRatingServiceApi;

    @Reference
    private StuffServiceApi stuffServiceApi;

    String PRODUCT_DATA_PATH = "src/main/resources/products.csv";
    String RATING_DATA_PATH = "src/main/resources/ratings.csv";

    public boolean readProductCSVData(){
        System.out.println(javaSparkContext);
        JavaRDD<String> productRDD = javaSparkContext.textFile(PRODUCT_DATA_PATH);
        JavaRDD<Product> product =productRDD.map( item -> {
            String[] attr = item.split("\\^");
            return new Product(Integer.valueOf(attr[0].trim()), attr[1].trim(), attr[4].trim(), attr[5].trim(), attr[6].trim());
        });

        Dataset<Row> productDF = sparkSession.createDataFrame(product,Product.class);
        productDF.show();
        List<StuffApiParam> stuffApiParams = new ArrayList<>();
        productDF.collectAsList().forEach( e ->{
            StuffApiParam stuffApiParam =new StuffApiParam();
            stuffApiParam.setId(e.getAs("productId"));
            stuffApiParam.setName(e.getAs("name"));
            stuffApiParam.setPictureId(e.getAs("imageUrl"));
            stuffApiParam.setDescription(e.getAs("tags"));
            stuffApiParams.add(stuffApiParam);
        });
        System.out.println(stuffApiParams.size());
        boolean result = stuffServiceApi.saveBatch(stuffApiParams);
        return result;
    }

    public boolean readRatingCSVData(){
        JavaRDD<String> ratingRDD = javaSparkContext.textFile(RATING_DATA_PATH);
        JavaRDD<ProductRating> rating = ratingRDD.map(item ->{
            String[] attr = item.split(",");
            return new ProductRating( Integer.valueOf(attr[0]), Integer.valueOf(attr[1]), Float.valueOf(attr[2]), Integer.valueOf(attr[3]));
        });
        Dataset<Row> ratingDF = sparkSession.createDataFrame(rating, ProductRating.class);
        ratingDF.show();
        List<ProductRatingParam> productRatingParams = new ArrayList<>();
        ratingDF.collectAsList().forEach( e-> {
            ProductRatingParam ratingParam = new ProductRatingParam();
            ratingParam.setUserId(e.getAs("userId"));
            ratingParam.setProductId(e.getAs("productId"));
            ratingParam.setScore(e.getAs("score"));
            Instant instant = Instant.ofEpochSecond(e.getInt(3));
            ratingParam.setTime(Timestamp.from(instant));
            productRatingParams.add(ratingParam);
        });
        System.out.println(productRatingParams.size());
        boolean result = productRatingServiceApi.insertByBatch(productRatingParams);
        return result;
    }
}
