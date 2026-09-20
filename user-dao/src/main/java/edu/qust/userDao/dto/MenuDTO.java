package edu.qust.userDao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Menu DTO
 *
 */
@Data
@Accessors(chain = true)
public class MenuDTO {
	/**
	 * Menu ID
	 */
	private Integer id;
	/**
	 * Parent ID; root menus use pid 0
	 */
	private Integer pid;
	/**
	 * Menu name
	 */
	private String name;
	/**
	 * Menu URL
	 */
	private String url;
}
