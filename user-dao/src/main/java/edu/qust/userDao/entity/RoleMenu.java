package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Role-menu relation
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class RoleMenu extends BaseEntity {

    /**
     * Role ID
     */
    private Integer roleId;

    /**
     * Menu ID
     */
    private Integer menuId;


}
