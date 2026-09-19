package edu.qust.common.base;

/**
 * 常量类
 *
 */
public interface Constant {

	/**
	 * 分隔符
	 *
	 */
	interface Separator {
		/**
		 * comma: ","
		 */
		String COMMA = ",";
		/**
		 * minus: "-"
		 */
		String MINUS = "-";
		/**
		 *  asterisk: "*"
		 */
		char ASTERISK = '*';
	}

	/**
	 * 错误信息
	 *
	 */
	interface ErrorMsg {
		String SYSTEM_INTERNAL_ERROR = "系统内部错误";
	}

	/**
	 * 图片验证码
	 *
	 */
	interface Captcha {
		String UUID_HEADER = "uuid";
	}

	/**
	 * session
	 *
	 */
	interface EmailCaptcha {
		/**
		 * email captcha redis key prefix
		 */
		String REDIS_KEY_PREFIX = "email_captcha-";
	}
}
