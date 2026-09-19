package edu.qust.userService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userDao.entity.UserRecommendation;
import edu.qust.userDao.mapper.UserRecsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserRecsService extends ServiceImpl<UserRecsMapper, UserRecommendation> implements IService<UserRecommendation> {


    public List<UserRecommendation> recommendByUserId(int userId ){

        LambdaQueryWrapper<UserRecommendation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRecommendation::getUserId,userId);

        return this.list(queryWrapper);
    }
}
