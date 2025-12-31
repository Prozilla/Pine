package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.util.function.mapper.Mapper;

/**
 * A property whose value can be changed.
 */
public interface MutableProperty<T> extends Property<T> {
	
	/**
	 * Replaces the value with a new value and returns its previous value.
	 * @param value The new value
	 * @return The previous value.
	 */
	default T swapValue(T value) {
		T previousValue = getValue();
		setValue(value);
		return previousValue;
	}
	
	/**
	 * Applies a mapper to the value of this property.
	 * @param mapper The mapper to apply
	 * @return {@code true} if the value was changed.
	 */
	default boolean modifyValue(Mapper<T, T> mapper) {
		return setValue(mapper.map(getValue()));
	}
	
	/**
	 * Sets the value of this property.
	 * @param value The new value
	 * @return {@code true} if the value was changed.
	 */
	boolean setValue(T value);
	
	/**
	 * Returns a property whose value is bound to the value of this property, but can't be modified.
	 * @return An unmodifiable property based on the value of this mutable property.
	 */
	default Property<T> viewProperty() {
		return this;
	}
	
}
