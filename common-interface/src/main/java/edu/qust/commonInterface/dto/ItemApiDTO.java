package edu.qust.commonInterface.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import edu.qust.common.enums.ItemStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * item api dto
 *
 */
@Data
@Accessors(chain = true)
public class ItemApiDTO implements Serializable {
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
	 * Payment Time
	 */
	private LocalDateTime payTime;

	/**
	 * Rental Days
	 */
	private Integer rentDay;
	/**
	 * Return Time
	 */
	private LocalDateTime endTime;
	/**
	 * Status (0: applying; 1: rejected; 2: renting; 3: returned)
	 */
	private ItemStatusEnum status;
	/**
	 * Owner name
	 */
	private String ownerName;
	/**
	 * Renter
	 */
	private String renterName;
}
