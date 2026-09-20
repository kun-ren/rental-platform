package edu.qust.userDao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class StuffDTO {
	private Integer stuffId;
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
	private Integer status;
	/**
	 * Rental date
	 */
	private LocalDate createTime;
	/**
	 * Rental Days
	 */
	private Integer rentDay;
	/**
	 * Renter
	 */
	private String renterName;
}
