package dev.prozilla.pine.common;

import java.lang.annotation.*;

/**
 * The class this interface delegate to.
 *
 * <p>This interface has a getter that returns an instance of the class.
 * Every other method has a default implementation which delegates to the corresponding method on the instance of the class</p>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ProviderOf {
	Class<?> value();
}
