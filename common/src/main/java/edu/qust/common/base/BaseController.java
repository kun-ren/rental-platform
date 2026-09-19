package edu.qust.common.base;

/**
 * base controller
 *
 */
public class BaseController {

	/**
	 * redirect to url
	 *
	 * @param url url
	 * @return "forward:" + url
	 */
	protected String redirect(String url) {
		return "redirect:" + url;
	}

	/**
	 * forward to url
	 *
	 * @param url url
	 * @return "forward:" + url
	 */
	protected String forward(String url) {
		return "forward:" + url;
	}

}
