package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.Property;

/**
 * A property whose value can be changed.
 */
public interface MutableProperty<T> extends Property<T> {
	
	/**
	 * Replaces the value with a new value and returns its previous value.
	 * @param value The new value
	 * @return The previous value.
	 */
	default T replaceValue(T value) {
		T previousValue = getValue();
		setValue(value);
		return previousValue;
	}
	
	/**
	 * Sets the value of this property.
	 * @param value The new value
	 * @return {@code true} if the value was changed.
	 */
	boolean setValue(T value);
	
}
