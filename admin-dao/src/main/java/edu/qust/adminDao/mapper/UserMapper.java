package edu.qust.adminDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.adminDao.entity.User;

import java.util.List;

/**
 * User table Mapper
 *
 */
public interface UserMapper extends BaseMapper<User> {
	List<User> list();
}
