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
     * 图片id
     */
    private String pictureId;

    /**
     * 物品所有者ID
     */
    private Integer userId;
}
