package edu.qust.userService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.qust.userService.CategoryService;
import edu.qust.userService.param.CategoryParam;
import edu.qust.common.base.Response;
import edu.qust.common.enums.EnableEnum;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.CategoryServiceApi;
import edu.qust.commonInterface.dto.CategoryApiDTO;
import edu.qust.commonInterface.dto.CategorySelectApiDTO;
import edu.qust.commonInterface.param.CategoryApiParam;
import edu.qust.userDao.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CategoryService Impl
 *
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryServiceApi {
	@Resource
	private CategoryService categoryService;

	/**
	 * Get all category information
	 *
	 * @return CategoryApiDTO list
	 */
	@Override
	public List<CategoryApiDTO> listCategoryApiDTO() {
		return categoryService.list(
				new QueryWrapper<Category>().lambda().eq(Category::getMark, EnableEnum.YES.getValue())
		).stream().map(c -> BeanUtil.map(c, CategoryApiDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * Add Category
	 *
	 * @param categoryApiParam categoryApiParam
	 * @return common.base.Response
	 */
	@Override
	public Response add(CategoryApiParam categoryApiParam) {
		return categoryService.add(BeanUtil.map(categoryApiParam, CategoryParam.class));
	}

	/**
	 * Update a category
	 *
	 * @param categoryApiParam categoryApiParam
	 * @return common.base.Response
	 */
	@Override
	public Response modify(CategoryApiParam categoryApiParam) {
		return categoryService.modify(BeanUtil.map(categoryApiParam, CategoryParam.class));
	}

	@Override
	public Response delete(Integer id) {
		return categoryService.delete(id);
	}

	/*@Override
	public List<CategorySelectApiDTO> listCategorySelectApiDTO() {
		return categoryService.listThreeLevelCategory().stream()
				.map(c -> BeanUtil.map(c, CategorySelectApiDTO.class))
				.collect(Collectors.toList());
	}*/
	@Override
	public List<CategorySelectApiDTO> listCategorySelectApiDTO() {
		return categoryService.listThreeLevelCategory().stream()
				.map(
						c -> {
							CategorySelectApiDTO categorySelectApiDTO = new CategorySelectApiDTO();
							BeanUtils.copyProperties(c,categorySelectApiDTO);
							return categorySelectApiDTO;
						})
				.collect(Collectors.toList());
	}
}
