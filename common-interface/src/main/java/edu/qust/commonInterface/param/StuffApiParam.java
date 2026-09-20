package edu.qust.commonInterface.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class StuffApiParam implements Serializable {



    private Integer id;

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
