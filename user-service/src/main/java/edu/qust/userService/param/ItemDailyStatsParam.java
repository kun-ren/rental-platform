package edu.qust.userService.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Daily rental statistics param
 *
 */
@Data
@Accessors(chain = true)
public class ItemDailyStatsParam {
	/**
	 * Start creation date
	 */
	private String beginAddDate;
	/**
	 * End creation date
	 */
	private String endAddDate;
	/**
	 * Category ID
	 */
	private Integer categoryId;
	/**
	 * Status
	 */
	private Integer status;
}
