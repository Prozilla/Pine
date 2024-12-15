package dev.prozilla.pine.core.entity.prefab;

import dev.prozilla.pine.core.component.Component;

import java.lang.annotation.*;

/**
 * The default components of a prefab for an entity.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Components {
	Class<? extends Component>[] value();
}
