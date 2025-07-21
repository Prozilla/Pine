package dev.prozilla.pine.common.property;

import java.util.Objects;
import java.util.Random;

/**
 * A property with a value that may change in certain circumstances.
 * @param <T> The type of property
 */
public abstract class VariableProperty<T> {
	
	protected static final Random random = new Random();
	
	/**
	 * Returns the value of this property.
	 * @return The value of this property.
	 */
	public abstract T getValue();
	
	/**
	 * Checks if this property has any value.
	 * @return {@code true} if the value of this property is not {@code null}.
	 */
	public boolean exists() {
		return getValue() != null;
	}
	
	/**
	 * Checks if this property has a given value.
	 * @param value The value to check for
	 * @return {@code true} if the value of this property is equal to {@code value}.
	 */
	public boolean hasValue(T value) {
		return Objects.equals(getValue(), value);
	}

}
