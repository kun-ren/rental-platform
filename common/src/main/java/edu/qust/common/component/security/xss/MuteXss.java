package edu.qust.common.component.security.xss;

import java.lang.annotation.*;

/**
 * Method that bypasses XSS protection
 * <p>
 *     Use @Getter(onMethod_={@MuteXss}) with Lombok
 * </p>
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MuteXss {
}
