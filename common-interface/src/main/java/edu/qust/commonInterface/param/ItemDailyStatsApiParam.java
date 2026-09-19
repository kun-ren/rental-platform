package edu.qust.commonInterface.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 租用项日统计 api param
 *
 */
@Data
@Accessors(chain = true)
public class ItemDailyStatsApiParam implements Serializable {
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
