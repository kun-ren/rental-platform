package edu.qust.recommender.config;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.commonInterface.ProductRecsServiceApi;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class StaticApi implements Serializable {


    @Reference
    private ProductRecsServiceApi productRecsServiceApi;

    @Reference
    private ProductRatingServiceApi productRatingServiceApi;


    public  ProductRecsServiceApi getProductRecsServiceApi(){
        return productRecsServiceApi;
    }

    public ProductRatingServiceApi getProductRatingServiceApi(){
        return productRatingServiceApi;
    }
}
