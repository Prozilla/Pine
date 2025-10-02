package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.FloatProperty;

public interface MutableFloatProperty extends MutableProperty<Float>, FloatProperty {
	
	@Override
	default boolean setValue(Float value) {
		return set(value);
	}
	
	boolean set(float value);
	
}
