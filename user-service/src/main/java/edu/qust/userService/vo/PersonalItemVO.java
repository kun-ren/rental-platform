package edu.qust.userService.vo;

import edu.qust.common.base.BaseVO;
import edu.qust.common.enums.ItemStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PersonalItemVO extends BaseVO {
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
	private ItemStatusEnum status;
	/**
	 * Payment Time
	 */
	private LocalDateTime payTime;
}
