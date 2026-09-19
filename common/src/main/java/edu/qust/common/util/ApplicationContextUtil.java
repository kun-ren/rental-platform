package edu.qust.common.util;

import org.springframework.context.ApplicationContext;

/**
 * ApplicationContext Util
 *
 */
public class ApplicationContextUtil {
	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtil.applicationContext = applicationContext;
	}

	/**
	 * 根据类型获取Bean
	 *
	 * @param requiredType 需要获取的bean类型
	 * @param <T>          需要获取的bean类型
	 * @return Bean
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}
}
