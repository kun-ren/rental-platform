package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.dto.RoleResourceDTO;
import edu.qust.userDao.entity.RoleResource;

import java.util.List;

/**
 * 角色资源关系 Mapper
 *
 */
public interface RoleResourceMapper extends BaseMapper<RoleResource> {
	/**
	 * query all RoleResourceDTO
	 *
	 * @return RoleResourceDTO list
	 */
	List<RoleResourceDTO> listRoleResourceDTO();
}
