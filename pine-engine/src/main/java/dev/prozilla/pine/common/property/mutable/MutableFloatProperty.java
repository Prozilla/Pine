package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.common.util.function.mapper.FloatMapper;

/**
 * A property with a float value that can be changed.
 */
public interface MutableFloatProperty extends FloatProperty, MutableProperty<Float> {
	
	@Override
	default boolean setValue(Float value) {
		return set(ObjectUtils.unbox(value));
	}
	
	/**
	 * Replaces the value with a new value and returns its previous value.
	 * @param value The new value
	 * @return The previous value.
	 */
	default float swap(float value) {
		float previousValue = get();
		set(value);
		return previousValue;
	}
	
	/**
	 * Applies a mapper to the value of this property.
	 * @param mapper The mapper to apply
	 * @return {@code true} if the value was changed.
	 */
	default boolean modify(FloatMapper mapper) {
		return set(mapper.map(get()));
	}
	
	/**
	 * Sets the value of this property.
	 * @param value The new value
	 * @return {@code true} if the value was changed.
	 */
	boolean set(float value);
	
	@Override
	default FloatProperty viewProperty() {
		return this::get;
	}
	
}
