package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Menu
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    /**
     * Parent ID; root menus use pid 0
     */
    private Integer pid;

    /**
     * Menu name
     */
    private String name;

    /**
     * Menu URL
     */
    private String url;


}
