package edu.qust.userDao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.qust.common.base.BaseEntity;

/**
 * Role
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Role extends BaseEntity {

    /**
     * Role identifier
     */
    private String identifier;

    /**
     * Role name
     */
    private String name;

    /**
     * Role description
     */
    private String description;


}
