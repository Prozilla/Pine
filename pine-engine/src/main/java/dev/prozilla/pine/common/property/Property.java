package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.exception.InvalidObjectException;
import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
import dev.prozilla.pine.common.property.fixed.FixedProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.function.Functor;
import dev.prozilla.pine.common.util.function.mapper.Mapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A property with a value that may change in certain circumstances.
 * @param <T> The type of property
 */
@FunctionalInterface
public interface Property<T> extends Functor<T> {
	
	/**
	 * Returns the value of this property.
	 * @return The value of this property.
	 */
	T getValue();
	
	/**
	 * Checks if the value of this property is {@code null}.
	 * @return {@code true} if the value of this property is {@code null}.
	 */
	default boolean isNull() {
		return !isNotNull();
	}
	
	/**
	 * Checks if the value of this property is not {@code null}.
	 * @return {@code true} if the value of this property is not {@code null}.
	 */
	default boolean isNotNull() {
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
	 * Returns the value of this property, if it is not {@code null}, otherwise throws an exception.
	 * @return The value of this property
	 * @throws InvalidObjectException If the value of this property is {@code null}.
	 */
	default @NotNull T requireValue() throws InvalidObjectException {
		return Checks.isNotNull(getValue(), "value");
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
	
	@Override
	default <S> Property<S> map(Mapper<T, S> mapper) {
		return () -> mapper.map(getValue());
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
	 * @see #isNotNull()
	 */
	default BooleanProperty isNotNullProperty() {
		return this::isNotNull;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @return A boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @see #hasValue(Object) 
	 */
	default BooleanProperty hasValueProperty(T value) {
		return () -> hasValue(value);
	}
	
	/**
	 * Returns a fixed property whose value is the current value of this property, at the time of calling this method.
	 * @return A fixed property with the current value of this property.
	 */
	default FixedProperty<T> snapshot() {
		return new FixedObjectProperty<>(getValue());
	}
	
	/**
	 * Returns the value of a given property, or {@code null} if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @return The value
	 * @param <T> The type of value
	 */
	@Contract("null -> null")
	static <T> T getValueOf(Property<T> property) {
		return getValueOf(property, null);
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property or its value is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property or its value is {@code null}.
	 * @return The value
	 * @param <T> The type of value
	 */
	@Contract("null, _ -> param2")
	static <T> T getValueOf(Property<T> property, T defaultValue) {
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
