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
	
	/**
	 * Returns the value of a given property, or {@code null} if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @return The value
	 * @param <T> The type of value
	 */
	public static <T> T getValue(VariableProperty<T> property) {
		return getValue(property, null);
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property or its value is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property or its value is {@code null}.
	 * @return The value
	 * @param <T> The type of value
	 */
	public static <T> T getValue(VariableProperty<T> property, T defaultValue) {
		if (property == null) {
			return defaultValue;
		}
		
		T value = property.getValue();
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
}
