package edu.qust.commonInterface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleApiDTO implements Serializable {
    private String username;
    private String password;
    private String roleIdentifierConcat;
}
