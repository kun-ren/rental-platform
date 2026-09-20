package edu.qust.commonInterface.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import edu.qust.common.enums.ItemStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Daily rental statistics API DTO
 *
 */
@Data
@Accessors(chain = true)
public class ItemDailyStatsApiDTO implements Serializable {
	/**
	 * End creation date
	 */
	private LocalDate addDate;
	/**
	 * Category ID
	 */
	private Integer categoryId;
	/**
	 * Status
	 */
	private ItemStatusEnum status;
	/**
	 * Total count
	 */
	private Integer totalCount;
	/**
	 * Total Deposit
	 */
	private BigDecimal totalDeposit;
	/**
	 * Total Rental
	 */
	private BigDecimal totalRental;
}
