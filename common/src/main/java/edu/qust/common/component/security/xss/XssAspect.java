package edu.qust.common.component.security.xss;

import edu.qust.common.exception.ContentIllegalException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.lang.reflect.Method;

/**
 * XSS aspect
 *
 */
@Aspect
@Component
public class XssAspect {

	@Pointcut("@annotation(edu.qust.common.component.security.xss.EnableXss)")
	private void enableXss() {

	}


	/**
	 * XSS aspect advice
	 *
	 * <pre>
	 * Intercept String and String-array getters on BaseVO subclasses and apply HTML escaping
	 * Skip methods annotated with @MuteXss
	 * </pre>
	 *
	 * @param joinPoint joinPoint
	 * @throws Throwable Throwable
	 */
	//Inspection placeholder
	//Not effective
	//@Around("within(edu.qust.common.base.BaseVO+) &&(execution(public String *.get*(..)) || execution(public String[] *.get*(..)))")
	//@Around("within(edu.qust.common.base.BaseVO+) &&(execution( * get*(..)))")
	@Before("enableXss()")
	public void htmlEscape(JoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		String stuffParam = args[0].toString();
		String origin  = stuffParam;
		if (stuffParam != null) {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			if (!method.isAnnotationPresent(MuteXss.class)) {
				if (String.class == stuffParam.getClass()) {
					stuffParam = HtmlUtils.htmlEscape(stuffParam);
				}
			}
		}
		if ( ! origin.equals(stuffParam) ) {
			throw new ContentIllegalException("Contains invalid characters");
		}
		return ;
	}
}
