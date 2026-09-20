package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Item
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Stuff extends BaseEntity {

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
     * Image ID
     */
    private String pictureId;

    /**
     * Item owner ID
     */
    private Integer userId;


}
