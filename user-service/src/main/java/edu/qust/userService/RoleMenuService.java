package edu.qust.userService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userDao.entity.RoleMenu;
import edu.qust.userDao.mapper.RoleMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 角色菜单关系 service
 *
 */
@Slf4j
@Service
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IService<RoleMenu> {

}
