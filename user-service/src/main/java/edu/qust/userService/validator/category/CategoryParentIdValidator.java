package edu.qust.userService.validator.category;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import edu.qust.userService.CategoryService;
import edu.qust.common.enums.CategoryLevelEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Category ParentId Validator
 *
 */
@Component
public class CategoryParentIdValidator extends ValidatorHandler<Integer> implements Validator<Integer> {
	@Resource
	private CategoryService categoryService;

	@Override
	public boolean validate(ValidatorContext context, Integer parentId) {
		if (parentId == 0) {
			return true;
		}
		// 父类别必须存在且不能为三级类别
		if (categoryService.existsLevelNotEquals(parentId, CategoryLevelEnum.THREE.getCode())) {
			return true;
		} else {
			context.addError(ValidationError.create("父类别非法操作，否则请联系管理员"));
			return false;
		}
	}

}