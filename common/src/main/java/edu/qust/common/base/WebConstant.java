package edu.qust.common.base;

/**
 * webModule constants
 *
 */
public interface WebConstant {

	/**
	 * Role identifier
	 *
	 */
	interface RoleIdentifier {
		/**
		 * ROOTRole identifier
		 */
		String ROOT = "ROOT";
	}

	interface Session {
		/**
		 * Menus stored after successful loginsession key
		 */
		String MENU_VO_LIST_SESSION_KEY = "session-menu-vo-list";
		String CURRENT_USER_ID_SESSION_KEY = "session-current-user-id";
	}
}
