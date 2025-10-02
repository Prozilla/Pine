package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.IntProperty;

public interface MutableIntProperty extends MutableProperty<Integer>, IntProperty {
	
	@Override
	default boolean setValue(Integer value) {
		return set(value);
	}
	
	boolean set(int value);
	
}
