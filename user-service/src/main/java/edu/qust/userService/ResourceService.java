package edu.qust.userService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userDao.entity.Resource;
import edu.qust.userDao.mapper.ResourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Resource service
 *
 */
@Slf4j
@Service
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> implements IService<Resource> {

}
