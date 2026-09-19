package edu.qust.userService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import edu.qust.commonInterface.StuffServiceApi;
import edu.qust.commonInterface.param.StuffApiParam;
import edu.qust.userDao.entity.Stuff;
import edu.qust.userService.StuffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StuffServiceApiImpl implements StuffServiceApi {


    @Autowired
    private StuffService stuffService;
    @Override
    public boolean saveBatch(List<StuffApiParam> stuffApiParams) {
        List<Stuff> stuffs = new ArrayList<>();
        stuffApiParams.forEach(e ->{
            Stuff  stuff= new Stuff();
            stuff.setName(e.getName());
            stuff.setCategoryId(4);
            stuff.setDeposit(BigDecimal.valueOf(50));
            stuff.setRental(BigDecimal.valueOf(500));
            stuff.setStatus(0);
            stuff.setDescription(e.getDescription());
            stuff.setPictureId(e.getPictureId());
            stuff.setUserId(9);
            stuff.completeAddParam(1);
            stuff.setId(e.getId());
            stuffs.add(stuff);
        });
        boolean result = stuffService.saveBatch(stuffs);

        log.info(stuffs.stream().limit(5).collect(Collectors.toList()).toString());
        return result;
    }
}
