package edu.qust.commonInterface.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Daily rental statistics API parameter
 *
 */
@Data
@Accessors(chain = true)
public class ItemDailyStatsApiParam implements Serializable {
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
