package edu.qust.userService.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Menu view object
 * <p>
 * Administrators manage this data, so inheriting BaseVO for XSS protection is unnecessary.
 * <p/>
 */
@Data
@Accessors(chain = true)
public class MenuVO implements Serializable {

	/**
	 * Child menus
	 */
	private List<MenuVO> children;
	/**
	 * Menu name
	 */
	private String name;
	/**
	 * Menu URL
	 */
	private String url;
}
