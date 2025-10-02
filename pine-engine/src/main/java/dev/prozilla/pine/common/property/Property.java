package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * A property with a value that may change in certain circumstances.
 * @param <T> The type of property
 */
@FunctionalInterface
public interface Property<T> {
	
	/**
	 * Returns the value of this property.
	 * @return The value of this property.
	 */
	T getValue();
	
	/**
	 * Checks if this property has any value.
	 * @return {@code true} if the value of this property is not {@code null}.
	 */
	default boolean exists() {
		return getValue() != null;
	}
	
	/**
	 * Checks if this property has a given value.
	 * @param value The value to check for
	 * @return {@code true} if the value of this property is equal to {@code value}.
	 */
	default boolean hasValue(T value) {
		return Objects.equals(getValue(), value);
	}
	
	/**
	 * Returns the value of this property, or {@code defaultValue} if the value is {@code null}.
	 * @param defaultValue The default value to use
	 * @return The value of this property, or {@code defaultValue} if the value is {@code null}.
	 */
	@Contract("null -> fail; !null -> !null")
	default T getValueOr(T defaultValue) {
		return Objects.requireNonNullElse(getValue(), defaultValue);
	}
	
	/**
	 * Returns a property whose value is the value of this property, or {@code defaultValue} if the value of this property is {@code null}.
	 * @return A property whose value is never {@code null}.
	 */
	@Contract("_ -> new")
	default Property<T> replaceNull(T defaultValue) {
		Checks.isNotNull(defaultValue, "defaultValue");
		return () -> getValueOr(defaultValue);
	}
	
	/**
	 * Returns a string property whose value is the string representation of the value of this property.
	 *
	 * <p>If the value of this property is {@code null}, the value of the string property with also be {@code null}.</p>
	 * @return A string property based on the value of this property.
	 */
	@Contract("-> new")
	default StringProperty toStringProperty() {
		return () -> StringUtils.toString(getValue());
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is not {@code null} and vice versa.
	 * @return A boolean property whose value is {@code true} if the value of this property is not {@code null} and vice versa.
	 */
	default BooleanProperty existsProperty() {
		return this::exists;
	}
	
	/**
	 * Returns the value of a given property, or {@code null} if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @return The value
	 * @param <T> The type of value
	 */
	@Contract("null -> null")
	static <T> T getPropertyValue(Property<T> property) {
		return getPropertyValue(property, null);
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property or its value is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property or its value is {@code null}.
	 * @return The value
	 * @param <T> The type of value
	 */
	@Contract("null, _ -> param2")
	static <T> T getPropertyValue(Property<T> property, T defaultValue) {
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
