package edu.qust.common.base;

/**
 * Constants
 *
 */
public interface Constant {

	/**
	 * Separators
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
	 * Error message
	 *
	 */
	interface ErrorMsg {
		String SYSTEM_INTERNAL_ERROR = "Internal system error";
	}

	/**
	 * Image CAPTCHA
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
