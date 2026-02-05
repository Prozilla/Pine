package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.exception.InvalidObjectException;
import dev.prozilla.pine.common.property.compared.ComparedObjectProperty;
import dev.prozilla.pine.common.property.compared.ComparedProperty;
import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
import dev.prozilla.pine.common.property.fixed.FixedProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.function.Functor;
import dev.prozilla.pine.common.util.function.mapper.Mapper;
import dev.prozilla.pine.common.util.function.mapper.ToBooleanMapper;
import dev.prozilla.pine.common.util.function.mapper.ToFloatMapper;
import dev.prozilla.pine.common.util.function.mapper.ToIntMapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

/**
 * A property with a value determined by a {@link #getValue()} function.
 *
 * <p>A derived property is a property created from another property, that makes use of that property's value.</p>
 * <p>A fixed property is a property with a value that never changes. Any property can be converted to a {@link FixedProperty} using {@link #snapshot()}.</p>
 * <span>
 *     <p>Properties use composition over inheritance for two main reasons:</p>
 *     <ul>
 *         <li><p>Primitive properties (boolean, float, int) have different implementations from object properties, because their values are passed around as primitives to avoid (un)boxing.</p></li>
 *         <li><p>Special object properties (e.g., {@link StringProperty}) can have additional functionality specific to that type of object.</p></li>
 *     </ul>
 * </span>
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
	 * @param defaultValue The value to replace {@code null} with.
	 * @return A property whose value is never {@code null}.
	 * @throws InvalidObjectException If {@code defaultValue} is {@code null}.
	 */
	@Contract("_ -> new")
	default Property<T> replaceNull(T defaultValue) throws InvalidObjectException {
		return map(Mapper.replaceNull(defaultValue));
	}
	
	/**
	 * Returns a string property whose value is the string representation of the value of this property.
	 *
	 * <p>If the value of this property is {@code null}, the value of the string property with also be {@code null}.</p>
	 * @return A string property based on the value of this property.
	 */
	@Contract("-> new")
	default StringProperty toStringProperty() {
		return mapToString(StringUtils::toString);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is not {@code null} and vice versa.
	 * @return A boolean property whose value is {@code true} if the value of this property is not {@code null} and vice versa.
	 * @see #isNotNull()
	 */
	default BooleanProperty isNotNullProperty() {
		return derive(this::isNotNull);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @return A boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @see #hasValue(Object) 
	 */
	default BooleanProperty hasValueProperty(T value) {
		return derive(() -> hasValue(value));
	}
	
	/**
	 * Returns a fixed property whose value is the current value of this property, at the time of calling this method.
	 * @return A fixed property with the current value of this property.
	 */
	default FixedProperty<T> snapshot() {
		return FixedProperty.fromValue(getValue());
	}
	
	/**
	 * Compares the value of this property with a given value.
	 * @see #compareWith(Comparator, FixedProperty)
	 */
	default IntProperty compareWith(Comparator<T> comparator, T value) {
		return compareWith(comparator, new FixedObjectProperty<>(value));
	}
	
	/**
	 * Compares this property with a property with a fixed value.
	 * @see #compareWith(Comparator, Property)
	 */
	default IntProperty compareWith(Comparator<T> comparator, FixedProperty<T> other) {
		return compareWith(comparator, (Property<T>)other);
	}
	
	/**
	 * Returns an integer property whose value is based on the comparison of the value of this property and another property.
	 * @param comparator The comparing function
	 * @param other The property to compare with
	 * @return An integer property whose value is based on the comparison of the value of this property and another property.
	 * @see ComparedProperty
	 */
	default IntProperty compareWith(Comparator<T> comparator, Property<T> other) {
		return new ComparedObjectProperty<>(this, comparator, other);
	}
	
	/**
	 * Returns an integer property whose value is the hash code of the value of this property.
	 * @return An integer property whose value is the hash code of the value of this property.
	 * @see Objects#hashCode(Object)
	 */
	default IntProperty hashCodeProperty() {
		return mapToInt(Objects::hashCode);
	}
	
	/**
	 * Returns a property that maps the value of this property to a boolean.
	 * @param mapper The function to use
	 * @return The mapped property.
	 * @see #map(Mapper)
	 */
	default BooleanProperty mapToBoolean(ToBooleanMapper<T> mapper) {
		return derive(() -> mapper.mapToBoolean(getValue()));
	}
	
	/**
	 * Returns a property that maps the value of this property to a float.
	 * @param mapper The function to use
	 * @return The mapped property.
	 * @see #map(Mapper)
	 */
	default FloatProperty mapToFloat(ToFloatMapper<T> mapper) {
		return derive(() -> mapper.mapToFloat(getValue()));
	}
	
	/**
	 * Returns a property that maps the value of this property to an integer.
	 * @param mapper The function to use
	 * @return The mapped property.
	 * @see #map(Mapper)
	 */
	default IntProperty mapToInt(ToIntMapper<T> mapper) {
		return derive(() -> mapper.mapToInt(getValue()));
	}
	
	/**
	 * Returns a property that maps the value of this property to a string.
	 * @param mapper The function to use
	 * @return The mapped property.
	 * @see #map(Mapper)
	 */
	default StringProperty mapToString(Mapper<T, String> mapper) {
		return derive(() -> mapper.map(getValue()));
	}
	
	/**
	 * Returns a property that applies a function to the value of this property.
	 * @param mapper The function to apply
	 * @return The mapped property.
	 * @param <S> The type of value to map to
	 */
	@Override
	default <S> Property<S> map(Mapper<T, S> mapper) {
		return derive(() -> mapper.map(getValue()));
	}
	
	/**
	 * Derives a boolean property from this property.
	 * @param property The property to derive
	 * @return The derived property.
	 * @see #derive(Property)
	 */
	default BooleanProperty derive(BooleanProperty property) {
		return property;
	}
	
	/**
	 * Derives a float property from this property.
	 * @param property The property to derive
	 * @return The derived property.
	 * @see #derive(Property)
	 */
	default FloatProperty derive(FloatProperty property) {
		return property;
	}
	
	/**
	 * Derives an integer property from this property.
	 * @param property The property to derive
	 * @return The derived property.
	 * @see #derive(Property)
	 */
	default IntProperty derive(IntProperty property) {
		return property;
	}
	
	/**
	 * Derives a string property from this property.
	 * @param property The property to derive
	 * @return The derived property.
	 * @see #derive(Property)
	 */
	default StringProperty derive(StringProperty property) {
		return property;
	}
	
	/**
	 * Derives a property from this property.
	 *
	 * <p>The derived property should depend on (the value of) this property.</p>
	 *
	 * <p>If this is a fixed property, derived properties will also be fixed. The value of this property will never change, so any property that depends on it should also never change.</p>
	 * @param property The property to derive
	 * @return The derived property.
	 * @param <S> The type of value of the derived property
	 */
	default <S> Property<S> derive(Property<S> property) {
		return property;
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
