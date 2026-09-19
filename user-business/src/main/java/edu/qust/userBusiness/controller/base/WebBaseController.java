package edu.qust.userBusiness.controller.base;

import edu.qust.userBusiness.common.WebConstant;
import org.springframework.security.core.Authentication;
import edu.qust.common.base.BaseController;
import edu.qust.common.exception.ResourceForbiddenException;
import edu.qust.userService.util.AuthUtil;

import javax.servlet.http.HttpSession;

/**
 * Web层基础控制器
 *
 */
public class WebBaseController extends BaseController {

	/**
	 * 获取当前用户ID，如果时ROOT用户，返回null
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
				throw new ResourceForbiddenException("没有权限");
			}
		}
		return userId;
	}

	/**
	 * 当前用户ID
	 *
	 * @param session session
	 * @return java.lang.Integer
	 */
	protected Integer currentUserId(HttpSession session) {
		Integer userId = (Integer) session.getAttribute(WebConstant.Session.CURRENT_USER_ID_SESSION_KEY);
		if (userId == null) {
			throw new ResourceForbiddenException("没有权限");
		}
		return userId;
	}
}
