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
	 * Get a bean by type
	 *
	 * @param requiredType Required bean type
	 * @param <T>          Required bean type
	 * @return Bean
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}
}
