package edu.qust.userService.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * stuff param
 *
 */
@Data
@Accessors(chain = true)
public class StuffParam {
	/**
	 * Category ID
	 */
	private Integer categoryId;

	/**
	 * Item Name
	 */
	private String name;

	/**
	 * Item description
	 */
	private String description;

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
	private Integer status;

	/**
	 * Item owner ID
	 */
	private Integer userId;
}
