package edu.qust.userService.util;

import edu.qust.common.enums.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Authentication and authorization utilities
 *
 */
public class AuthUtil {
	/**
	 * Log outURL
	 */
	public static final String LOGOUT_URL = "/logout";

	/**
	 * Whether the user has the ROOT role
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
