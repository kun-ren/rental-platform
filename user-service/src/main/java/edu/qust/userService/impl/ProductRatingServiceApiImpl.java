package edu.qust.userService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.commonInterface.param.ProductRatingParam;
import edu.qust.userDao.entity.ProductRating;
import edu.qust.userService.ProductRatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductRatingServiceApiImpl implements ProductRatingServiceApi, Serializable {

    @Autowired
    ProductRatingService productRatingService;
    @Override
    public boolean insertByBatch(List<ProductRatingParam> productRatingParams) {
        List<ProductRating> productRatings = productRatingParams.stream().map( e -> {
        return BeanUtil.map(e,ProductRating.class);}).collect(Collectors.toList());
        productRatingService.insertBatchIgnore(productRatings);
        //boolean result = productRatingService.saveBatch(productRatings);

        log.info(productRatings.stream().limit(5).collect(Collectors.toList()).toString());
        return true;
    }



    @Override
    public List<ProductRatingParam> list() {
        List<ProductRating> ratings = productRatingService.list();
        List<ProductRatingParam> result = ratings.stream().map(
                e -> BeanUtil.map(e,ProductRatingParam.class)).collect(Collectors.toList());

        return result;
    }

    @Override
    public ProductRatingParam getRating(int userId, int productId) {

        ProductRating productRating = productRatingService.getRating(userId, productId);
        ProductRatingParam result = BeanUtil.map(productRating,ProductRatingParam.class);
        return result;
    }
}
