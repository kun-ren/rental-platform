package edu.qust.userService;

import com.baidu.unbiz.fluentvalidator.*;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.baidu.unbiz.fluentvalidator.registry.Registry;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userService.param.CategoryParam;
import edu.qust.userService.validator.category.CategoryValidatorGroup;
import edu.qust.userService.vo.CategorySelectVO;
import edu.qust.userService.vo.CategoryVO;
import edu.qust.common.base.Response;
import edu.qust.common.enums.CategoryLevelEnum;
import edu.qust.common.enums.EnableEnum;
import edu.qust.common.util.BeanUtil;
import edu.qust.userDao.entity.Category;
import edu.qust.userDao.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Category service
 *
 */
@Slf4j
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements IService<Category> {
	@Resource
	private javax.validation.Validator hibernateValidator;
	@Resource
	private Registry springApplicationContextRegistry;
	@Resource
	private CategoryMapper categoryMapper;

	/**
	 * whether category name exists
	 *
	 * @param name category name
	 * @return true: exists
	 */
	public boolean existsName(String name) {
		return this.count(
				new QueryWrapper<Category>().lambda()
						.eq(Category::getName, name)
						.eq(Category::getMark, EnableEnum.YES.getValue())
		) > 0;
	}

	/**
	 * whether category name equals {id} and level not equals {level}
	 *
	 * @param id    category id
	 * @param level category level
	 * @return true: exists
	 */
	public boolean existsLevelNotEquals(Integer id, int level) {
		return this.count(
				new QueryWrapper<Category>().lambda()
						.eq(Category::getId, id)
						.ne(Category::getParentId, level)
						.eq(Category::getMark, EnableEnum.YES.getValue())
		) > 0;
	}

	/**
	 * Add Category
	 *
	 * @param categoryParam categoryParam
	 * @return Response
	 */
	public Response add(CategoryParam categoryParam) {
		Result result = FluentValidator.checkAll(CategoryValidatorGroup.Add.class)
				.on(categoryParam, new HibernateSupportedValidator<CategoryParam>()
						.setHiberanteValidator(hibernateValidator))
				.configure(springApplicationContextRegistry)
				.on(categoryParam)
				.doValidate()
				.result(ResultCollectors.toSimple());
		if (!result.isSuccess()) {
			log.info("add category invalid param: {}", result.getErrors());
			return Response.fail(result.getErrors().get(0));
		}
		Category category = BeanUtil.map(categoryParam, Category.class);
		this.save(category);
		log.info("a category saved: {}", category);
		return Response.SUCCESS;
	}

	/**
	 * Update a category
	 *
	 * @param categoryParam categoryParam
	 * @return Response
	 */
	public Response modify(CategoryParam categoryParam) {
		final String categoryIdErrorMsg = "Invalid category ID operation. Contact an administrator if the problem persists";
		if (categoryParam.getId() == null) {
			return Response.fail(categoryIdErrorMsg);
		}
		Category originCategory = this.getById(categoryParam.getId());
		if (originCategory == null) {
			return Response.fail(categoryIdErrorMsg);
		}
		Result result = FluentValidator.checkAll(CategoryValidatorGroup.Modify.class)
				.on(categoryParam, new HibernateSupportedValidator<CategoryParam>()
						.setHiberanteValidator(hibernateValidator))
				.configure(springApplicationContextRegistry)
				.on(categoryParam)
				.on(categoryParam.getLevel(), new ValidatorHandler<Integer>() {
					@Override
					public boolean validate(ValidatorContext context, Integer level) {
						// Prevent moving a category below its original level
						if (level > originCategory.getLevel()) {
							context.addError(ValidationError.create("The category level cannot be lowered"));
							return false;
						}
						return true;
					}
				})
				.doValidate()
				.result(ResultCollectors.toSimple());
		if (!result.isSuccess()) {
			log.info("modify category invalid param: {}", result.getErrors());
			return Response.fail(result.getErrors().get(0));
		}
		Category category = BeanUtil.map(categoryParam, Category.class);
		this.updateById(category);
		log.info("a category modified: {}", category);
		return Response.SUCCESS;
	}

	public Response delete(Integer id) {
		Category category = this.getOne(
				new QueryWrapper<Category>().lambda()
						.eq(Category::getId, id)
						.eq(Category::getMark, EnableEnum.YES.getValue())
		);
		if (category == null) {
			return Response.fail("The category does not exist");
		}
		Set<Integer> deleteIdSet = new HashSet<>();
		deleteIdSet.add(id);
		if (category.getLevel() == CategoryLevelEnum.TWO.getCode()) {
			deleteIdSet.addAll(
					this.list(new QueryWrapper<Category>().lambda()
							.eq(Category::getParentId, id)
							.eq(Category::getMark, EnableEnum.YES.getValue())
					).stream()
							.map(Category::getId)
							.collect(Collectors.toSet())
			);
		} else if (category.getLevel() == CategoryLevelEnum.ONE.getCode()) {
			List<Category> secondLevelCategoryList = this.list(new QueryWrapper<Category>().lambda()
					.eq(Category::getParentId, id)
					.eq(Category::getMark, EnableEnum.YES.getValue())
			);
			deleteIdSet.addAll(
					secondLevelCategoryList.stream()
							.map(Category::getId)
							.collect(Collectors.toSet())
			);
			for (Category secondLevelCategory : secondLevelCategoryList) {
				List<Category> thirdLevelCategoryList = this.list(new QueryWrapper<Category>().lambda()
						.eq(Category::getParentId, secondLevelCategory.getId())
						.eq(Category::getMark, EnableEnum.YES.getValue())
				);
				if(CollectionUtils.isNotEmpty(thirdLevelCategoryList)) {
					deleteIdSet.addAll(
							thirdLevelCategoryList.stream()
									.map(Category::getId)
									.collect(Collectors.toSet())
					);
				}
			}
		}
		categoryMapper.batchDelete(deleteIdSet);
		log.info("deleted categories' id: {}", deleteIdSet);
		return Response.SUCCESS;
	}

	public List<CategoryVO> listCategoryVO() {
		return this.listAllCategory().stream()
				.map(c -> BeanUtil.map(c, CategoryVO.class))
				.collect(Collectors.toList());
	}

	public List<CategorySelectVO> listCategorySelectVO() {
		return this.listThreeLevelCategory().stream()
				.map(c -> BeanUtil.map(c, CategorySelectVO.class))
				.collect(Collectors.toList());
	}

	public List<Category> listThreeLevelCategory() {
		return this.list(
				new QueryWrapper<Category>().lambda()
						.eq(Category::getMark, EnableEnum.YES.getValue())
						.eq(Category::getStatus, EnableEnum.YES.getValue())
						.eq(Category::getLevel, CategoryLevelEnum.THREE.getCode())
		);
	}

	public List<Category> listAllCategory() {
		return this.list(
				new QueryWrapper<Category>().lambda()
						.eq(Category::getMark, EnableEnum.YES.getValue())
						.eq(Category::getStatus, EnableEnum.YES.getValue())
		);
	}

	public Map<Integer, String> getIdNameMap() {
		List<Category> categoryList = this.listAllCategory();
		Map<Integer, String> idNameMap = new HashMap<>(categoryList.size());
		for (Category category : categoryList) {
			idNameMap.put(category.getId(), category.getName());
		}
		return idNameMap;
	}
}
