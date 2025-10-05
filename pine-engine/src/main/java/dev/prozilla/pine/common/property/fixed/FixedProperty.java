package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.util.StringUtils;

/**
 * A property with a value that never changes.
 */
@FunctionalInterface
public interface FixedProperty<T> extends Property<T> {
	
	@Override
	default FixedStringProperty toStringProperty() {
		return new FixedStringProperty(StringUtils.toString(getValue()));
	}
	
	@Override
	default FixedBooleanProperty existenceProperty() {
		return BooleanProperty.fromValue(exists());
	}
	
}
