package edu.qust.common.component.security.sensitive;

import edu.qust.common.exception.ContentIllegalException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import edu.qust.common.base.Constant;
import edu.qust.common.component.security.sensitive.plugin.SensitiveFilter;

import java.lang.reflect.Method;

/**
 * Sensitive-word filtering aspect
 *
 */
@Aspect
@Component
public class SensitiveWordAspect {

	@Pointcut("@annotation(EnableSensitive)")
	private void enableSensitiveWordApsect(){}
	/**
	 * Sensitive-word filtering advice
	 *
	 * @param joinPoint joinPoint
	 *
	 *
	 */
	//@Around("within(edu.qust.common.base.BaseVO+)")
	//@Around("execution(public String edu.qust.common.base.BaseVO+.get*(..))")
	//@Around("within(edu.qust.common.base.BaseVO+) &&(execution(public String *.get*(..)) || execution(public String[] *.get*(..)))")

	@Before("enableSensitiveWordApsect()")
	public void sensitiveWordFilter(JoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		String stuffParam = args[0].toString();
		String original = stuffParam;
		if (stuffParam != null) {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			if (!method.isAnnotationPresent(MuteSensitive.class)) {
				// handle string or sting[]
				if (String.class == stuffParam.getClass()) {
					stuffParam = SensitiveFilter.DEFAULT.filter( stuffParam, Constant.Separator.ASTERISK);
				}
			}
		}
		if ( ! original.equals(stuffParam) ){
			throw new ContentIllegalException("Contains prohibited content");
		}
		return ;
	}

}
