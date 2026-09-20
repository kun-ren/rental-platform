package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.dto.UserRoleDTO;
import edu.qust.userDao.entity.UserRole;

import java.util.List;

/**
 * User-role relation Mapper
 *
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
	/**
	 * query all UserRoleDTO
	 *
	 * @return UserRoleDTO list
	 */
	List<UserRoleDTO> listUserRoleDTO();
}
