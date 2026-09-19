package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 出租项
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Item extends BaseEntity {

    /**
     * 租用者ID
     */
    private Integer userId;

    /**
     * 物品编号
     */
    private Integer stuffId;

    /**
     * 审批时间
     */
    private LocalDateTime approvalTime;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 租用天数
     */
    private Integer rentDay;

    /**
     * 归还时间
     */
    private LocalDateTime endTime;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 状态（0：申请中；1：不通过；2:待支付；3：租用中；4： 已归还）
     */
    private Integer status;

    /**
     * 添加日期
     */
    private LocalDate addDate;


}
