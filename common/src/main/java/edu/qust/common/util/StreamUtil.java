package edu.qust.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * stream util
 *
 */
@Slf4j
public class StreamUtil {
	private StreamUtil() {
	}

	/**
	 * stream Distinct by property
	 *
	 * @param keyExtractor keyExtractor
	 * @return java.util.function.Predicate<T>
	 * @see <a href="https://stackoverflow.com/questions/23699371/java-8-distinct-by-property">Java 8 Distinct by property</a>
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}
}
