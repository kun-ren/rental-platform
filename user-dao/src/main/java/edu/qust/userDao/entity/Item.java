package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Rental record
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Item extends BaseEntity {

    /**
     * Renter ID
     */
    private Integer userId;

    /**
     * Item ID
     */
    private Integer stuffId;

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
     * Application Time
     */
    private LocalDateTime applyTime;

    /**
     * Status (0: applying; 1: rejected; 2: awaiting payment; 3: renting; 4: returned)
     */
    private Integer status;

    /**
     * Creation date
     */
    private LocalDate addDate;


}
