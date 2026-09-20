package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * User-role relation
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class UserRole extends BaseEntity {

    /**
     * User ID
     */
    private Integer userId;

    /**
     * Role ID
     */
    private Integer roleId;


}
