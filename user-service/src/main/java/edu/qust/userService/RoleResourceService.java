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
 * Role-resource relation service
 *

 */
@Slf4j
@Service
public class RoleResourceService extends ServiceImpl<RoleResourceMapper, RoleResource> implements IService<RoleResource> {
	@javax.annotation.Resource
	private RoleResourceMapper roleResourceMapper;

	/**
	 * Load every role-resource relation and grant ROOT access to all resources
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
