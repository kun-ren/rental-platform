package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Role-resource relation
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class RoleResource extends BaseEntity {

    /**
     * Role ID
     */
    private Integer roleId;

    /**
     * Resource ID
     */
    private Integer resourceId;


}
