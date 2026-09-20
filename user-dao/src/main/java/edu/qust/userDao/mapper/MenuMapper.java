package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.dto.MenuDTO;
import edu.qust.userDao.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Menu mapper
 *
 */
public interface MenuMapper extends BaseMapper<Menu> {

	List<MenuDTO> listMenuDTOByRoleIdSet(@Param("roleIdSet") Set<Integer> roleIdSet);
}
