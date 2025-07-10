package dev.prozilla.pine.common;

import java.lang.annotation.*;

/**
 * The class whose methods this interface provides access to.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ContextOf {
	Class<?> value();
}
