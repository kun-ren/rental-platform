package edu.qust.userService.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.qust.commonInterface.UserRecsServiceApi;
import edu.qust.commonInterface.param.UserRecommendationParam;
import edu.qust.commonInterface.param.UserRecsParam;
import edu.qust.userDao.entity.UserRecommendation;
import edu.qust.userService.UserRecsService;
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
public class UserRecsServiceApiImpl implements UserRecsServiceApi, Serializable {
    @Autowired
    UserRecsService userRecsService;


    @Override
    public List<UserRecsParam> list() {
        List<UserRecommendation> userRecommendations = userRecsService.list();
        Map<Integer, List<UserRecommendation>> map =
                userRecommendations.stream().collect(Collectors.groupingBy(UserRecommendation::getUserId));
        List<UserRecsParam> userRecsParams = new ArrayList<>();
        map.forEach( (userId, recs) -> {
            UserRecsParam userRecsParam = new UserRecsParam();
            userRecsParam.setUserId(userId);
            List<UserRecommendationParam> recommendationParams = new ArrayList<>();
            recs.forEach( r -> {
                UserRecommendationParam rec= new UserRecommendationParam();
                rec.setProductId(r.getProductId());
                rec.setScore(r.getScore());
                recommendationParams.add(rec);
            });
            userRecsParam.setRecs(recommendationParams);
            userRecsParams.add(userRecsParam);
        });

        return userRecsParams;
    }

    @Override
    public boolean save(List<UserRecsParam> userRecsParams) {

        List<UserRecommendation> userRecommendations = userRecsService.list();
        Map<Integer, List<UserRecommendation>> map =
                userRecommendations.stream().collect(Collectors.groupingBy(UserRecommendation::getUserId));

        List<Integer> userIds = map.keySet().stream().collect(Collectors.toList());
        if(userIds.size() > 0 ) {
            LambdaQueryWrapper<UserRecommendation> old = new LambdaQueryWrapper<>();
            old.eq(UserRecommendation::getUserId, userIds);
            userRecsService.remove(old);
        }

        List<UserRecommendation> userRecommendationList = new ArrayList<>();
        userRecsParams.forEach( userRec ->{
            userRec.getRecs().forEach( rec -> {
                UserRecommendation r = new UserRecommendation();
                r.setUserId(userRec.getUserId());
                r.setProductId(rec.getProductId());
                r.setScore(rec.getScore());
                userRecommendationList.add(r);
            });
        });
        boolean result = userRecsService.saveBatch(userRecommendationList);
        return result;
    }
}
