package edu.qust.userService.vo;

import edu.qust.common.base.BaseVO;
import edu.qust.common.enums.StuffStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Rental item view object
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StuffInVO extends BaseVO {
	/**
	 * stuff id
	 */
	private Integer id;
	/**
	 * Category Name
	 */
	private String categoryName;
	/**
	 * Item Name
	 */
	private String stuffName;
	/**
	 * Owner name
	 */
	private String ownerName;
	/**
	 * Deposit(rmb)
	 */
	private BigDecimal deposit;

	/**
	 * Rental (RMB/day)
	 */
	private BigDecimal rental;
	/**
	 * Item status (0: available; 1: application pending; 2: rented; 3: not offered)
	 */
	private StuffStatusEnum status;
	/**
	 * Expected Return Date
	 */
	private LocalDate shouldReturnDate;


	private double rate;
}
