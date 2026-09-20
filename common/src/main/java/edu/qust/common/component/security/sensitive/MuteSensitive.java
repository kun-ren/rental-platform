package edu.qust.common.component.security.sensitive;

import java.lang.annotation.*;

/**
 * Method that bypasses sensitive-word filtering
 * <p>
 *     Use @Getter(onMethod_={@MuteSensitive}) with Lombok
 * </p>
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MuteSensitive {
}
