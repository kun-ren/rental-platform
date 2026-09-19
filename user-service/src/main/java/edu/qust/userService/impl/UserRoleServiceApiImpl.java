package edu.qust.userService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.UserRoleServiceApi;
import edu.qust.commonInterface.dto.UserRoleApiDTO;
import edu.qust.userDao.dto.UserRoleDTO;
import edu.qust.userDao.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserRoleServiceApiImpl implements UserRoleServiceApi, Serializable {


    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public List<UserRoleApiDTO> listUserRole() {
        List<UserRoleDTO> userRoleDTOList = userRoleMapper.listUserRoleDTO();
        List<UserRoleApiDTO> userRoleApiDTOList = userRoleDTOList.stream().map(c ->
            BeanUtil.map(c,UserRoleApiDTO.class)
        ).collect(Collectors.toList());
        return  userRoleApiDTOList;
    }
}
