package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.FloatProperty;

/**
 * A property with a float value that can be changed.
 */
public interface MutableFloatProperty extends MutableProperty<Float>, FloatProperty {
	
	@Override
	default boolean setValue(Float value) {
		return set(value);
	}
	
	/**
	 * Replaces the value with a new value and returns its previous value.
	 * @param value The new value
	 * @return The previous value.
	 */
	default float replaceValue(float value) {
		float previousValue = get();
		set(value);
		return previousValue;
	}
	
	boolean set(float value);
	
}
