package edu.qust.userDao.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 租用项日统计 query object
 *
 */
@Data
@Accessors(chain = true)
public class ItemDailyStatsQuery {
	/**
	 * 开始创建日期
	 */
	private String beginAddDate;
	/**
	 * 结束创建日期
	 */
	private String endAddDate;
	/**
	 * 类别ID
	 */
	private Integer categoryId;
	/**
	 * 状态
	 */
	private Integer status;
}
