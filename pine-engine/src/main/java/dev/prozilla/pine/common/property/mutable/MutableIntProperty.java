package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.IntProperty;

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
	
	boolean set(int value);
	
}
