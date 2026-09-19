package edu.qust.common.util;

import com.baidu.unbiz.easymapper.MapperFactory;

/**
 * Bean Util
 *
 */
public class BeanUtil {

	/**
	 * Bean Mapping util
	 *
	 * @param source      source
	 * @param targetClass targetClass
	 * @return target
	 */
	public static <S, T> T map(S source, Class<T> targetClass) {
		return MapperFactory.getCopyByRefMapper()
				.mapClass(source.getClass(), targetClass)
				.registerAndMap(source, targetClass);
	}

}
