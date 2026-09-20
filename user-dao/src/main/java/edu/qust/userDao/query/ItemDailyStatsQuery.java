package edu.qust.userDao.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Daily rental statistics query object
 *
 */
@Data
@Accessors(chain = true)
public class ItemDailyStatsQuery {
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
