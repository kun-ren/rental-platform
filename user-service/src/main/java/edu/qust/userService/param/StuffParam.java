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
	 * 类别编号
	 */
	private Integer categoryId;

	/**
	 * 物品名称
	 */
	private String name;

	/**
	 * 物品描述
	 */
	private String description;

	/**
	 * 押金(rmb)
	 */
	private BigDecimal deposit;

	/**
	 * 租金（rmb/day）
	 */
	private BigDecimal rental;

	/**
	 * 物品状态（0:未租；1:申请租用；2:已租;3:不出租）
	 */
	private Integer status;

	/**
	 * 物品所有者ID
	 */
	private Integer userId;
}
