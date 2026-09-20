package edu.qust.userService.validator.category;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import edu.qust.userService.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Category Name Validator
 *
 */
@Slf4j
@Component
public class CategoryNameValidator extends ValidatorHandler<String> implements Validator<String> {
	@Resource
	private CategoryService categoryService;

	@Override
	public boolean validate(ValidatorContext context, String name) {
		if (categoryService.existsName(name)) {
			context.addError(ValidationError.create("The category already exists"));
			return false;
		}
		return true;
	}

}
