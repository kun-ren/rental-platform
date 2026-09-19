package edu.qust.common.component.security.sensitive;

import java.lang.annotation.*;

/**
 * 不做敏感词过滤的方法
 * <p>
 *     使用‘@Getter(onMethod_={@MuteSensitive})’方式集成lombok
 * </p>
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MuteSensitive {
}