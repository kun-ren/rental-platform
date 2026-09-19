package edu.qust.adminService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.adminDao.entity.User;
import lombok.extern.slf4j.Slf4j;
import edu.qust.adminDao.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表 service
 *
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {
	@Resource
	private UserMapper userMapper;

	public List<User> listUsers() {
		return userMapper.list();
	}
}
