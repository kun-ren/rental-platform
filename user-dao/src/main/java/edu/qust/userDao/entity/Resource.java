package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Resource
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class Resource extends BaseEntity {

    /**
     * Resource name
     */
    private String name;

    /**
     * Resource description
     */
    private String description;

    /**
     * Resource URL
     */
    private String url;

    /**
     * HTTP method
     */
    private String method;


}
