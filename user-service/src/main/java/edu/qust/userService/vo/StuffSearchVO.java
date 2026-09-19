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
	 * 类别名称
	 */
	private String categoryName;
	/**
	 * 物品名称
	 */
	private String stuffName;
	/**
	 * 物品描述
	 */
	private String stuffDescription;
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
	private StuffStatusEnum status;
}
