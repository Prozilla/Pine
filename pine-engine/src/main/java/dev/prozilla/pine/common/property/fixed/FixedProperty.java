package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.util.StringUtils;

public interface FixedProperty<T> extends Property<T> {
	
	@Override
	default FixedStringProperty toStringProperty() {
		return new FixedStringProperty(StringUtils.toString(getValue()));
	}
	
	@Override
	default FixedBooleanProperty existsProperty() {
		return BooleanProperty.fromValue(exists());
	}
	
}
