package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Category
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Category extends BaseEntity {

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

	/**
	 * Status (1: enabled, 0: disabled)
	 */
	private Boolean status;


}
