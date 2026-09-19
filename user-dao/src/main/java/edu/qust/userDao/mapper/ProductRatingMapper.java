package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.entity.ProductRating;

import java.util.List;

public interface ProductRatingMapper extends BaseMapper<ProductRating> {


    public void insertBatchIgnore(List<ProductRating> productRatingList);
}
