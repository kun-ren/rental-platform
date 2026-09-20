package edu.qust.userService.param;

import edu.qust.userService.validator.auth.AuthValidatorGroup;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

/**
 * register param
 *
 */
@Data
@Accessors(chain = true)
public class RegisterParam {
	/**
	 * Username
	 */
	@NotEmpty(message = "This field is required", groups = AuthValidatorGroup.Register.class)
	@Size(min = 5, max = 15, message = "Length must be 5-15 characters. Do not include names, identity numbers, bank card numbers, or other private information", groups = AuthValidatorGroup.Register.class)
	@Pattern(regexp = "[0-9a-zA-Z\u4e00-\u9fa5_]+", message = "The username may contain Chinese or English letters, digits, and underscores", groups = AuthValidatorGroup.Register.class)
	private String username;

	/**
	 * Email
	 */
	@NotEmpty(message = "This field is required", groups = {AuthValidatorGroup.Register.class, AuthValidatorGroup.SendEmailCaptcha.class})
	@Size(max = 50, message = "The email address cannot exceed 50 characters",
			groups = {AuthValidatorGroup.Register.class, AuthValidatorGroup.SendEmailCaptcha.class})
	@Email(message = "The email address format is invalid", groups = {AuthValidatorGroup.Register.class, AuthValidatorGroup.SendEmailCaptcha.class})
	private String email;

	/**
	 * Gender (0: female, 1: male, 2: prefer not to say)
	 */
	@NotNull(message = "This field is required", groups = AuthValidatorGroup.Register.class)
	@Range(min = 0, max = 2, message = "Select a valid gender", groups = AuthValidatorGroup.Register.class)
	private Integer sex;

	/**
	 * Password
	 */
	@NotBlank(message = "This field is required", groups = AuthValidatorGroup.Register.class)
	@Size(min = 6, max = 16, message = "Length must be 6-16 characters", groups = {AuthValidatorGroup.Register.class, AuthValidatorGroup.ResetPassword.class})
	@Pattern(regexp = "[0-9a-zA-Z\\p{Punct} ”]+", message = "The password may contain letters, digits, and punctuation", groups = AuthValidatorGroup.Register.class)
	private String password;

	/**
	 * Confirm Password
	 */
	@NotBlank(message = "This field is required", groups = {AuthValidatorGroup.Register.class, AuthValidatorGroup.ResetPassword.class})
	private String confirmedPassword;

	/**
	 * Email Verification Code
	 */
	@NotEmpty(message = "This field is required", groups = AuthValidatorGroup.Register.class)
	private String emailCaptcha;

	/**
	 * Role (2: Lessor, 3: Lessee)
	 */
	@NotNull(message = "This field is required", groups = AuthValidatorGroup.Register.class)
	@Range(min = 2, max = 3, message = "Select a valid role", groups = AuthValidatorGroup.Register.class)
	private Integer role;
}
