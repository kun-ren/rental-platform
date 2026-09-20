package edu.qust.userBusiness.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * CAPTCHA validation exception
 *
 * @author yanganyu
 * @date 2019/3/2 17:07
 */
public class CaptchaValidationException extends AuthenticationException {

	public CaptchaValidationException(String msg) {
		super(msg);
	}
}
