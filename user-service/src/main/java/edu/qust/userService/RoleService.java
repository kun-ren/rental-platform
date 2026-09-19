package edu.qust.userService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userDao.entity.Role;
import edu.qust.userDao.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 角色 service
 *
 */
@Slf4j
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IService<Role> {

}
