package edu.qust.userBusiness.controller;

import com.baidu.unbiz.fluentvalidator.*;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.google.common.collect.Iterables;
import edu.qust.common.base.BaseController;
import edu.qust.common.base.Constant;
import edu.qust.common.base.Response;
import edu.qust.common.util.StringUtil;
import edu.qust.userDao.entity.User;
import edu.qust.userService.MailService;
import edu.qust.userService.UserService;
import edu.qust.userService.param.RegisterParam;
import edu.qust.userService.validator.auth.AuthValidatorGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;
import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;

/**
 * Registration controller
 *
 */
@Slf4j
@Controller
public class RegisterController extends BaseController {
	@Resource
	private javax.validation.Validator hibernateValidator;
	@Resource
	private UserService userService;
	@Resource
	private MailService mailService;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * Open the registration page
	 *
	 * @return page
	 */
	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	/**
	 * Submit a registration request
	 *
	 * @param registerParam register Param
	 * @param session       session
	 * @return response
	 */
	@PostMapping("/users")
	@ResponseBody
	public Response register(@RequestBody RegisterParam registerParam, HttpSession session) {
		log.info("get registerParam: {}", registerParam);
		//Data validation,(return the result)
		ComplexResult result = FluentValidator.checkAll(AuthValidatorGroup.Register.class)
				.failFast()
				.on(registerParam.getEmailCaptcha(), new ValidatorHandler<String>() {
					@Override
					public boolean validate(ValidatorContext context, String inputEmailCaptcha) {
						String errorMsg = "The verification code is incorrect. Check the email address or send a new code";
						if (inputEmailCaptcha == null) {
							context.addError(ValidationError.create(errorMsg).setField("emailCaptcha"));
							return false;
						}
						String redisKey = getEmailCaptchaRedisKey(session, registerParam.getEmail());
						String emailCaptcha = stringRedisTemplate.opsForValue().get(redisKey);
						stringRedisTemplate.delete(redisKey);
						if (!inputEmailCaptcha.equalsIgnoreCase(emailCaptcha)) {
							context.addError(ValidationError.create(errorMsg).setField("emailCaptcha"));
							return false;
						}
						return true;
					}
				})
				.on(registerParam,
						new HibernateSupportedValidator<RegisterParam>().setHiberanteValidator(hibernateValidator))
				.on(registerParam.getUsername(), new ValidatorHandler<String>() {
					@Override
					public boolean validate(ValidatorContext context, String username) {
						if (inMemoryUserDetailsManager.userExists(username)) {
							context.addError(ValidationError.create("This username is already in use").setField("username"));
							return false;
						}
						return true;
					}
				})
				.on(registerParam.getEmail(), new ValidatorHandler<String>() {
					@Override
					public boolean validate(ValidatorContext context, String email) {
						if (userService.existsEmail(email)) {
							context.addError(ValidationError.create("This email address is already in use").setField("email"));
							return false;
						}
						return true;
					}
				})
				.doValidate()
				.result(toComplex());
		if (!result.isSuccess()) {
			ValidationError validationError = Iterables.getFirst(result.getErrors(), null);
			if (validationError == null) {
				return Response.FAIL;
			} else {
				return Response.fail(validationError.getErrorMsg(), validationError.getField());
			}
		}
		if (!registerParam.getPassword().equals(registerParam.getConfirmedPassword())) {
			return Response.fail("The passwords do not match", "password");
		}
		// save user
		userService.save(getUserFrom(registerParam), registerParam.getRole());
		return Response.SUCCESS;
	}

	/**
	 * get user from RegisterParam
	 *
	 * @param registerParam registerParam
	 * @return User
	 */
	private User getUserFrom(RegisterParam registerParam) {
		User user = new User()
				.setUsername(registerParam.getUsername())
				.setPassword(new BCryptPasswordEncoder().encode(registerParam.getPassword()))
				.setEmail(registerParam.getEmail())
				.setSex(registerParam.getSex())
				.setStatus(true);
		user.completeAddParam(0);
		return user;
	}

	/**
	 * Send an email verification code
	 *
	 * @param email email
	 * @return Response
	 */
	@PostMapping("/emails/{email}/send-captcha")
	@ResponseBody
	public Response sendEmailCaptcha(@PathVariable String email, HttpSession session) {
		Result result = FluentValidator.checkAll(AuthValidatorGroup.SendEmailCaptcha.class)
				.failFast()
				.on(new RegisterParam().setEmail(email), new HibernateSupportedValidator<RegisterParam>().setHiberanteValidator(hibernateValidator))
				.doValidate()
				.result(toSimple());
		if (!result.isSuccess()) {
			return Response.fail(Iterables.getFirst(result.getErrors(), StringUtils.EMPTY));
		}
		if (userService.existsEmail(email)) {
			return Response.fail("This email address is already in use");
		}
		String captcha = StringUtil.randomString(6);
		//Store one email code per session ID and address in Redis so a valid code cannot be reused with another address
		log.info(captcha);
		String redisKey = this.getEmailCaptchaRedisKey(session, email);
		stringRedisTemplate.opsForValue().set(redisKey, captcha);
		stringRedisTemplate.expire(redisKey, 2, TimeUnit.MINUTES);
		// Render the email template
		Context context = new Context();
		context.setVariable("captcha", captcha);
		String emailContent = templateEngine.process("mail/email_captcha", context);
		// Send the email
		mailService.sendEmailAsync(email, StringUtil.format("{} is your rent-X registration verification code", captcha), emailContent);
		return Response.SUCCESS;
	}

	/**
	 * get email captcha redis key
	 *
	 * @param session HttpSession
	 * @param email email
	 * @return email captcha redis key
	 */
	private String getEmailCaptchaRedisKey(HttpSession session, String email) {
		return Constant.EmailCaptcha.REDIS_KEY_PREFIX + session.getId().substring(0, 5) + Constant.Separator.MINUS + email;
	}

	/**
	 * Check whether the username already exists
	 *
	 * @param username username
	 * @return response
	 */
	@PostMapping("/users/{username}/check-exists")
	@ResponseBody
	public Response checkUsernameExist(@PathVariable String username) {
		if (inMemoryUserDetailsManager.userExists(username)) {
			return Response.fail("This username is already in use");
		}
		return Response.SUCCESS;
	}
}
