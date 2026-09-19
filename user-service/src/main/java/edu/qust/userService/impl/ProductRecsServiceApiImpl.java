package edu.qust.userService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ProductRecsServiceApi;
import edu.qust.commonInterface.param.ProductRecsParam;
import edu.qust.commonInterface.param.ProductRecommendationParam;
import edu.qust.userDao.entity.ProductRecommendation;
import edu.qust.userService.ProductRecsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductRecsServiceApiImpl implements ProductRecsServiceApi, Serializable {


    @Autowired
    ProductRecsService productRecsService;

    @Override
    public List<ProductRecsParam> list() {
        List<ProductRecommendation> productRecommendations = productRecsService.list();
        Map<Integer, List<ProductRecommendation>> map = productRecommendations.stream().collect(Collectors.groupingBy(ProductRecommendation::getParentProductId));
        List<ProductRecsParam> productRecs = new ArrayList<>();
        map.forEach( (parentId, rec) -> {
            ProductRecsParam recs = new ProductRecsParam();
            recs.setProductId(parentId);
            List<ProductRecommendationParam> r = rec.stream().map(e -> BeanUtil.map(e, ProductRecommendationParam.class)).collect(Collectors.toList());
            recs.setRecs(r);
            productRecs.add(recs);
        });

        return productRecs;
    }

    @Override
    public boolean save(List<ProductRecsParam> productRecsParams) {

        //删除旧的推荐列表
        List<ProductRecommendation> productRecommendations = productRecsService.list();
        Map<Integer, List<ProductRecommendation>> map = productRecommendations.stream().collect(Collectors.groupingBy(ProductRecommendation::getParentProductId));
        Set<Integer> parentIds = map.keySet();
        if (parentIds.size() > 0) {
            LambdaQueryWrapper<ProductRecommendation> old = new LambdaQueryWrapper<>();
            old.eq(ProductRecommendation::getParentProductId, parentIds);
            productRecsService.remove(old);
        }

        //存入新的
        List<ProductRecommendation> productRecommendationList = new ArrayList<>();
        productRecsParams.forEach( e-> {
            e.getRecs().forEach(rec -> {
                ProductRecommendation r = new ProductRecommendation();
                r.setParentProductId(e.getProductId());
                r.setProductId(rec.getProductId());
                r.setScore(rec.getScore());
                productRecommendationList.add(r);
            });
        });
        boolean result = productRecsService.saveBatch(productRecommendationList);
        log.info("success");
        return result;
    }
}
