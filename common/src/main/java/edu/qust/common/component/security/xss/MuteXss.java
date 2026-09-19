package edu.qust.common.component.security.xss;

import java.lang.annotation.*;

/**
 * 不做XSS防御的方法
 * <p>
 *     使用‘@Getter(onMethod_={@MuteXss})’方式集成lombok
 * </p>
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MuteXss {
}
