package edu.qust.userService.util;

import edu.qust.common.enums.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 认证授权工具
 *
 */
public class AuthUtil {
	/**
	 * 登出URL
	 */
	public static final String LOGOUT_URL = "/logout";

	/**
	 * 是否拥有ROOT角色
	 *
	 * @param authentication authentication
	 * @return true: has root role
	 */
	public static boolean hasRootRole(Authentication authentication) {
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if (RoleEnum.ROOT.getName().equals(authority.getAuthority().substring(5))) {
				return true;
			}
		}
		return false;
	}

}
