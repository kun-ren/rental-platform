package edu.qust.userService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.common.base.Constant;
import edu.qust.common.base.WebConstant;
import edu.qust.userDao.dto.RoleResourceDTO;
import edu.qust.userDao.entity.RoleResource;
import edu.qust.userDao.mapper.RoleResourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色资源关系 service
 *

 */
@Slf4j
@Service
public class RoleResourceService extends ServiceImpl<RoleResourceMapper, RoleResource> implements IService<RoleResource> {
	@javax.annotation.Resource
	private RoleResourceMapper roleResourceMapper;

	/**
	 * 查询所有资源角色关系，并给所有资源添加和ROOT角色的关系
	 *
	 * @return RoleResourceDTO list
	 */
	public List<RoleResourceDTO> listRoleResourceDTOPlusRoot() {
		List<RoleResourceDTO> roleResourceDTOList = roleResourceMapper.listRoleResourceDTO();
		for (RoleResourceDTO roleResourceDTO : roleResourceDTOList) {
			if(StringUtils.isNotBlank(roleResourceDTO.getRoleIdentifierConcat())) {
				roleResourceDTO.setRoleIdentifierConcat(roleResourceDTO.getRoleIdentifierConcat()
						+ Constant.Separator.COMMA + WebConstant.RoleIdentifier.ROOT);
			}
		}
		return roleResourceDTOList;
	}

}
