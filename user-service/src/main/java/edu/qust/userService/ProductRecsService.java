package edu.qust.userService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userDao.entity.ProductRecommendation;
import edu.qust.userDao.mapper.ProductRecsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductRecsService extends ServiceImpl<ProductRecsMapper, ProductRecommendation> implements IService<ProductRecommendation> {



    public List<ProductRecommendation> recommendByProductId(int productId ){
        LambdaQueryWrapper<ProductRecommendation> re = new LambdaQueryWrapper<>();
        re.eq(ProductRecommendation::getParentProductId,productId);
        return this.list(re);
    }


}
