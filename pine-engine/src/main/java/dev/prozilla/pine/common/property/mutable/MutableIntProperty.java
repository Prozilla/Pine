package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.util.function.mapper.IntMapper;

/**
 * A property with an integer value that can be changed.
 */
public interface MutableIntProperty extends IntProperty, MutableProperty<Integer> {
	
	@Override
	default boolean setValue(Integer value) {
		return set(value);
	}
	
	/**
	 * Replaces the value with a new value and returns its previous value.
	 * @param value The new value
	 * @return The previous value.
	 */
	default int swap(int value) {
		int previousValue = get();
		set(value);
		return previousValue;
	}
	
	/**
	 * Applies a mapper to the value of this property.
	 * @param mapper The mapper to apply
	 * @return {@code true} if the value was changed.
	 */
	default boolean modify(IntMapper mapper) {
		return set(mapper.map(get()));
	}
	
	/**
	 * Sets the value of this property.
	 * @param value The new value
	 * @return {@code true} if the value was changed.
	 */
	boolean set(int value);
	
	@Override
	default IntProperty viewProperty() {
		return this::get;
	}
	
}
