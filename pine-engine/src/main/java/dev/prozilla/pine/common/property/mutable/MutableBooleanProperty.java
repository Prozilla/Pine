package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.BooleanProperty;

public interface MutableBooleanProperty extends MutableProperty<Boolean>, BooleanProperty {
	
	@Override
	default boolean setValue(Boolean value) {
		return set(value);
	}
	
	boolean set(boolean value);
	
}
