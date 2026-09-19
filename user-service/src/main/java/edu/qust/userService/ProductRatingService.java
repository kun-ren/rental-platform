package edu.qust.userService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userDao.entity.ProductRating;
import edu.qust.userDao.mapper.ProductRatingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductRatingService extends ServiceImpl<ProductRatingMapper, ProductRating> implements IService<ProductRating> {


    @Autowired
    ProductRatingMapper productRatingMapper;

    public ProductRating getRating(int userId, int productId){
        LambdaQueryWrapper<ProductRating> productRating = new LambdaQueryWrapper<>();
        productRating.eq(ProductRating::getUserId,userId);
        productRating.eq(ProductRating::getProductId,productId);
        ProductRating rating = this.getOne(productRating);

        return rating;
    }


    public void insertBatchIgnore(List<ProductRating> productRatingList){
        productRatingMapper.insertBatchIgnore(productRatingList);
    }
}
