package edu.qust.userDao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ItemDTO {
	/**
	 * Rental record ID
	 */
	private Integer itemId;
	/**
	 * Item Name
	 */
	private String stuffName;
	/**
	 * Application Time
	 */
	private LocalDateTime applyTime;
	/**
	 * Approval Time
	 */
	private LocalDateTime approvalTime;

	/**
	 * Rental Days
	 */
	private Integer rentDay;
	/**
	 * Return Time
	 */
	private LocalDateTime endTime;
	/**
	 * Status (0: applying; 1: rejected; 2: awaiting payment; 3: renting; 4: returned)
	 */
	private Integer status;
	/**
	 * Owner name
	 */
	private String ownerName;
	/**
	 * Renter
	 */
	private String renterName;
	/**
	 * Payment Time
	 */
	private LocalDateTime payTime;
}
