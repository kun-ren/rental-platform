package edu.qust.userDao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.qust.common.base.BaseEntity;

/**
 * 角色
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Role extends BaseEntity {

    /**
     * 角色标识符
     */
    private String identifier;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;


}
