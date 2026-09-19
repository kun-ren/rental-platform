package edu.qust.userDao.dto;

import lombok.Data;

/**
 * UserRole DTO
 *
 */
@Data
public class UserRoleDTO {
	private String username;
	private String password;
	private String roleIdentifierConcat;
}
