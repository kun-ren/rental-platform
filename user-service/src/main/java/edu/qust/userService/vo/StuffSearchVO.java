package edu.qust.userService.vo;

import edu.qust.common.base.BaseVO;
import edu.qust.common.enums.StuffStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * stuff search VO
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StuffSearchVO extends BaseVO {
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
	 * Item description
	 */
	private String stuffDescription;
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
}
