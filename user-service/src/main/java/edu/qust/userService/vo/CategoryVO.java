package edu.qust.userService.vo;

import edu.qust.common.base.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * category VO
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CategoryVO extends BaseVO {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * Category Name
	 */
	private String name;

	/**
	 * Category Description
	 */
	private String description;

	/**
	 * Parent category ID (0 identifies a root category)
	 */
	private Integer parentId;

	/**
	 *  Category level (must be 1, 2, or 3)
	 */
	private Integer level;
}
