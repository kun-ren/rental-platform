package edu.qust.userService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userService.vo.MenuVO;
import edu.qust.common.enums.EnableEnum;
import edu.qust.common.enums.RoleEnum;
import edu.qust.common.util.BeanUtil;
import edu.qust.userDao.dto.MenuDTO;
import edu.qust.userDao.entity.Menu;
import edu.qust.userDao.mapper.MenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 service
 *
 */
@Slf4j
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> implements IService<Menu> {
	@Autowired
	private MenuMapper menuMapper;

	private List<MenuDTO> listAllMenuDTO() {
		return this.list(
				new QueryWrapper<Menu>().lambda()
						.eq(Menu::getMark, EnableEnum.YES.getValue())
		).stream().map(menu -> BeanUtil.map(menu, MenuDTO.class))
				.collect(Collectors.toList());
	}

	public List<MenuVO> listMenuVO(Set<Integer> roleIdSet) {
		List<MenuDTO> menuDTOList;
		if (roleIdSet.contains(RoleEnum.ROOT.getId())) {
			menuDTOList = this.listAllMenuDTO();
		} else {
			menuDTOList = menuMapper.listMenuDTOByRoleIdSet(roleIdSet);
		}
		return this.convertToMenuVOList(menuDTOList);
	}

	private List<MenuVO> convertToMenuVOList(List<MenuDTO> menuDTOList) {
		Map<Integer, List<MenuVO>> idListMap = new HashMap<>();
		Map<Integer, MenuVO> idMenuVOMap = new HashMap<>();
		for (MenuDTO menuDTO : menuDTOList) {
			if (menuDTO.getPid() == 0) {
				idMenuVOMap.put(menuDTO.getId(), BeanUtil.map(menuDTO, MenuVO.class));
			} else {
				List<MenuVO> menuVOList = idListMap.get(menuDTO.getPid());
				if (menuVOList == null) {
					menuVOList = new ArrayList<>();
				}
				menuVOList.add(BeanUtil.map(menuDTO, MenuVO.class));
				idListMap.put(menuDTO.getPid(), menuVOList);
			}
		}
		List<MenuVO> result = new ArrayList<>();
		for (Map.Entry<Integer, List<MenuVO>> entry : idListMap.entrySet()) {
			MenuVO menuVO = idMenuVOMap.get(entry.getKey());
			menuVO.setChildren(entry.getValue());
			result.add(menuVO);
		}
		return result;
	}
}
