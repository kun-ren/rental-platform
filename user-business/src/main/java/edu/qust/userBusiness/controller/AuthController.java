package edu.qust.userBusiness.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import edu.qust.common.base.Response;
import edu.qust.common.component.security.xss.MuteXss;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import edu.qust.common.base.Constant;
import edu.qust.userBusiness.exception.CaptchaValidationException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Authentication controller
 *
 */
@Slf4j
@Controller
public class AuthController {
	@Resource
	private Producer kaptchaProducer;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * Open the login page
	 *
	 * @return page
	 */

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/login/success")
	public Response login() {
		String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user != null){
			return new Response<String>(Response.CodeEnum.SUCCESS,user);
		}
		return Response.FAIL;
	}

	/**
	 * Open the forgot-password page
	 *
	 * @return page
	 */
	@GetMapping("/forget")
	public String forgetPassword() {
		return "reset";
	}

	/**
	 * Get the CAPTCHA as a Base64 string
	 *
	 * @param response response
	 */
	@GetMapping("/captcha")
	@MuteXss
	public void getCaptcha(HttpServletResponse response) {
		String uuid = UUID.randomUUID().toString();
		response.addHeader("Access-Control-Expose-Headers","uuid");
		response.setHeader(Constant.Captcha.UUID_HEADER, uuid);
		// create the text for the image
		String kaptchaProducerText = kaptchaProducer.createText();
		// store the text in the session
		log.debug("generate captcha: {}", kaptchaProducerText);
		String redisKey = Constants.KAPTCHA_SESSION_KEY + Constant.Separator.MINUS + uuid;
		stringRedisTemplate.opsForValue().set(redisKey, kaptchaProducerText);
		stringRedisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
		// create the image with the text
		BufferedImage bufferedImage = kaptchaProducer.createImage(kaptchaProducerText);
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 PrintWriter printWrite = response.getWriter()
		) {
			ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
			byte[] imageInByte = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.flush();
			String encodedString = Base64.getEncoder().encodeToString(imageInByte);
			printWrite.write(encodedString);
			printWrite.flush();
		} catch (IOException e) {
			log.error("Failed to generate the CAPTCHA", e);
		}
	}

	/**
	 * CAPTCHA validation failed
	 *
	 * @param session session
	 * @return page
	 */
	@GetMapping("/captcha/error")
	public String captchaError(HttpSession session, Model model) {
		//Spring Security stores the exception in the session by default.
		AuthenticationException authenticationException =
				(AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		//Check whether this is the custom CAPTCHA authentication exception
		if (authenticationException instanceof CaptchaValidationException) {
			//Store the CAPTCHA error in the request so it affects only this request, not the entire session
			model.addAttribute("captchaErrorMsg", authenticationException.getMessage());
		}
		return "login";
	}
}
