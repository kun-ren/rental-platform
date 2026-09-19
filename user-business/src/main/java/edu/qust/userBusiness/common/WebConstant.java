package edu.qust.userBusiness.common;

/**
 * web模块常量类
 *
 */
public interface WebConstant {

	/**
	 * 角色标识符
	 *
	 */
	interface RoleIdentifier {
		/**
		 * ROOT角色标识符
		 */
		String ROOT = "ROOT";
	}

	interface Session {
		/**
		 * 登录成功保存的菜单session key
		 */
		String MENU_VO_LIST_SESSION_KEY = "session-menu-vo-list";
		String CURRENT_USER_ID_SESSION_KEY = "session-current-user-id";
	}
}
