package edu.qust.userService.validator.auth;

/**
 * 认证校验分组
 *
 */
public class AuthValidatorGroup {
	/**
	 * 注册
	 *
	 */
	public interface Register {
	}

	/**
	 * 发送邮件验证码
	 *
	 */
	public interface SendEmailCaptcha {
	}

	/**
	 * 重置密码
	 *
	 */
	public interface ResetPassword {
	}

}
