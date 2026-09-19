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
 * Xss切面
 *
 */
@Aspect
@Component
public class XssAspect {

	@Pointcut("@annotation(edu.qust.common.component.security.xss.EnableXss)")
	private void enableXss() {

	}


	/**
	 * Xss切面方法
	 *
	 * <pre>
	 * 所有BaseVO子类种的'String getXxx()'或'String[] getXxx()'被调用时拦截，做html转义处理
	 * 如果方法有@MuteXss注解，则不处理
	 * </pre>
	 *
	 * @param joinPoint joinPoint
	 * @throws Throwable Throwable
	 */
	//你要看什么
	//不生效
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
			throw new ContentIllegalException("含有非法字符");
		}
		return ;
	}
}
