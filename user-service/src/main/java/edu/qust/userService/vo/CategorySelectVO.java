package edu.qust.userService.vo;

import edu.qust.common.base.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * category select vo
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CategorySelectVO extends BaseVO {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * Category Name
	 */
	private String name;
}
