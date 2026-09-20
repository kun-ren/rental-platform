package edu.qust.userService.param;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValidate;
import edu.qust.userService.validator.category.CategoryNameValidator;
import edu.qust.userService.validator.category.CategoryParentIdValidator;
import edu.qust.userService.validator.category.CategoryValidatorGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

/**
 * Category Param
 *
 */
@Data
@Accessors(chain = true)
public class CategoryParam {
	/**
	 * Category ID
	 */
	private Integer id;

	/**
	 * Category Name
	 */
	@NotBlank(message = "The category name is required", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	@Size(min = 1, max = 20, message = "The category name must contain 1-20 characters", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	@Pattern(regexp = "[0-9a-zA-Z\u4e00-\u9fa5_]+", message = "The category name may contain Chinese or English letters, digits, and underscores", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	@FluentValidate(value = {CategoryNameValidator.class}, groups = CategoryValidatorGroup.Add.class)
	private String name;

	/**
	 * Category Description
	 */
	@Size(max = 255, message = "The category description cannot exceed 255 characters", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	private String description;

	/**
	 * Parent category ID (0 identifies a root category)
	 */
	@NotNull(message = "Invalid parent-category operation. Contact an administrator if the problem persists", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	@FluentValidate(value = {CategoryParentIdValidator.class}, groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	private Integer parentId;

	/**
	 *  Category level (must be 1, 2, or 3)
	 */
	@NotNull(message = "Invalid category-level operation. Contact an administrator if the problem persists", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	@Min(value = 1, message = "Invalid category-level operation. Contact an administrator if the problem persists", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	@Max(value = 3, message = "Invalid category-level operation. Contact an administrator if the problem persists", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	private Integer level;

	/**
	 * Status (1: enabled, 0: disabled)
	 */
	@NotNull(message = "Invalid enabled-status operation. Contact an administrator if the problem persists", groups = {CategoryValidatorGroup.Add.class,CategoryValidatorGroup.Modify.class})
	private Boolean status;
}
