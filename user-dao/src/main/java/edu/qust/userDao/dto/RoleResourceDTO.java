package edu.qust.userDao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RoleResource DTO
 */
@Data
@Accessors(chain = true)
public class RoleResourceDTO {
	private String roleIdentifierConcat;
	private String resourceURL;
	private String resourceMethod;
}