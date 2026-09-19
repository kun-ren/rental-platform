package edu.qust.userService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.common.enums.EnableEnum;
import edu.qust.common.enums.RoleEnum;
import edu.qust.userDao.entity.User;
import edu.qust.userDao.entity.UserRole;
import edu.qust.userDao.mapper.UserMapper;
import edu.qust.userDao.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户表 service
 *
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {
	@Resource
	private UserMapper userMapper;
	@Resource
	private UserRoleMapper userRoleMapper;
	@Autowired
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;

	/**
	 * whether username exists
	 *
	 * @param username username to check
	 * @return true: exists
	 */
	public boolean existsUsername(String username) {
		return this.count(
				new QueryWrapper<User>().lambda()
						.eq(User::getUsername, username)
						.eq(User::getMark, EnableEnum.YES.getValue())
		) > 0;
	}

	/**
	 * whether email exists
	 *
	 * @param email email
	 * @return true: exists
	 */
	public boolean existsEmail(String email) {
		return this.count(
				new QueryWrapper<User>().lambda()
						.eq(User::getEmail, email)
						.eq(User::getMark, EnableEnum.YES.getValue())
		) > 0;
	}

	/**
	 * save user and user-role relation
	 *
	 * @param user   user
	 * @param roleId role id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void save(User user, Integer roleId) {
		userMapper.insert(user);
		UserRole userRole = new UserRole().setUserId(user.getId()).setRoleId(roleId);
		userRole.completeAddParam(0);
		userRoleMapper.insert(userRole);
		inMemoryUserDetailsManager.createUser(
				org.springframework.security.core.userdetails.User
						.withUsername(user.getUsername())
						.password(user.getPassword())
						.roles(RoleEnum.getById(roleId).toString())
						.build()
		);
	}

	public Integer getUserIdByName(String username) {
		return this.getOne(
				new QueryWrapper<User>().lambda()
						.eq(User::getUsername, username)
						.eq(User::getMark, EnableEnum.YES.getValue())
		).getId();
	}
}
