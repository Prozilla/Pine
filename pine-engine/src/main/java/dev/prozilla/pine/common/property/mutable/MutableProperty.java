package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.Property;

public interface MutableProperty<T> extends Property<T> {
	
	/**
	 * Sets the value of this property.
	 * @param value The new value
	 * @return {@code true} if the value was changed.
	 */
	boolean setValue(T value);
	
}
