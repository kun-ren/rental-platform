package edu.qust.userDao.entity;

import edu.qust.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * User table
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class User extends BaseEntity {

    /**
     * Username
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * Email
     */
    private String email;

    /**
     * Gender (0: female, 1: male, 2: prefer not to say)
     */
    private Integer sex;

    /**
     * Status (1: active, 0: locked)
     */
    private Boolean status;


}
