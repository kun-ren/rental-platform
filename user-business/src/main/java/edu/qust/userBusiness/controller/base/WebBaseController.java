package edu.qust.userBusiness.controller.base;

import edu.qust.userBusiness.common.WebConstant;
import org.springframework.security.core.Authentication;
import edu.qust.common.base.BaseController;
import edu.qust.common.exception.ResourceForbiddenException;
import edu.qust.userService.util.AuthUtil;

import javax.servlet.http.HttpSession;

/**
 * Web-layer base controller
 *
 */
public class WebBaseController extends BaseController {

	/**
	 * Get the current user ID; return null for the ROOT user
	 *
	 * @param authentication authentication
	 * @param session        session
	 * @return userId
	 */
	protected Integer currentNonRootUserId(Authentication authentication, HttpSession session) {
		Integer userId = null;
		if (!AuthUtil.hasRootRole(authentication)) {
			userId = (Integer) session.getAttribute(WebConstant.Session.CURRENT_USER_ID_SESSION_KEY);
			if (userId == null) {
				throw new ResourceForbiddenException("Permission denied");
			}
		}
		return userId;
	}

	/**
	 * Current user ID
	 *
	 * @param session session
	 * @return java.lang.Integer
	 */
	protected Integer currentUserId(HttpSession session) {
		Integer userId = (Integer) session.getAttribute(WebConstant.Session.CURRENT_USER_ID_SESSION_KEY);
		if (userId == null) {
			throw new ResourceForbiddenException("Permission denied");
		}
		return userId;
	}
}
