package edu.qust.commonInterface.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * category BO
 *
 */
@Data
@Accessors(chain = true)//Fluent accessors
public class CategoryApiDTO implements Serializable {
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

	/**
	 * Status (1: enabled, 0: disabled)
	 */
	private Boolean status;
}
