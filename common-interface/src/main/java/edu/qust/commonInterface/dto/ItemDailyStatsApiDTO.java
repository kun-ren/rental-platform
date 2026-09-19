package edu.qust.commonInterface.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import edu.qust.common.enums.ItemStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 租用项日统计 api dto
 *
 */
@Data
@Accessors(chain = true)
public class ItemDailyStatsApiDTO implements Serializable {
	/**
	 * 结束创建日期
	 */
	private LocalDate addDate;
	/**
	 * 类别ID
	 */
	private Integer categoryId;
	/**
	 * 状态
	 */
	private ItemStatusEnum status;
	/**
	 * 总数量
	 */
	private Integer totalCount;
	/**
	 * 押金总额
	 */
	private BigDecimal totalDeposit;
	/**
	 * 租金总额
	 */
	private BigDecimal totalRental;
}
