package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.compared.ComparedFloatProperty;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.common.util.function.comparator.FloatComparator;
import dev.prozilla.pine.common.util.function.mapper.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * A property with a float value.
 */
@FunctionalInterface
public interface FloatProperty extends NonNullProperty<Float> {
	
	FloatComparator DEFAULT_ORDER = FloatComparator.naturalOrder();
	
	@Override
	default Float getValueOr(Float defaultValue) {
		return getValue();
	}
	
	@Override
	@NotNull
	default Float requireValue() {
		return getValue();
	}
	
	@Override
	default Float getValue() {
		return get();
	}
	
	/**
	 * Returns the primitive value of this property.
	 * @return The primitive value of this property.
	 */
	float get();
	
	/**
	 * @see #hasValue(Object)
	 */
	default boolean has(float value) {
		return get() == value;
	}
	
	/**
	 * Checks if the value of this property is strictly positive.
	 * @return {@code true} if the value of this property is strictly positive.
	 */
	default boolean isStrictlyPositive() {
		return isGreaterThan(0);
	}
	
	/**
	 * Checks if the value of this property is strictly negative.
	 * @return {@code true} if the value of this property is strictly negative.
	 */
	default boolean isStrictlyNegative() {
		return isLessThan(0);
	}
	
	/**
	 * Checks if the value of this property is positive.
	 * @return {@code true} if the value of this property is positive.
	 */
	default boolean isPositive() {
		return isGreaterThanOrEqualTo(0);
	}
	
	/**
	 * Checks if the value of this property is negative.
	 * @return {@code true} if the value of this property is negative.
	 */
	default boolean isNegative() {
		return isLessThanOrEqualTo(0);
	}
	
	@Contract("_ -> this")
	@Override
	default FloatProperty replaceNull(Float defaultValue) {
		return this;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is {@code 0}.
	 * @return A boolean property based on whether the value of this property is {@code 0}.
	 * @see #hasProperty(float) 
	 */
	default BooleanProperty isZeroProperty() {
		return hasProperty(0);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is strictly positive.
	 * @return A boolean property based on whether the value of this property is strictly positive.
	 * @see #isStrictlyPositive()
	 */
	default BooleanProperty isStrictlyPositiveProperty() {
		return derive(this::isStrictlyPositive);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is strictly negative.
	 * @return A boolean property based on whether the value of this property is strictly negative.
	 * @see #isStrictlyNegative()
	 */
	default BooleanProperty isStrictlyNegativeProperty() {
		return derive(this::isStrictlyNegative);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is positive.
	 * @return A boolean property based on whether the value of this property is positive.
	 * @see #isPositive()
	 */
	default BooleanProperty isPositiveProperty() {
		return derive(this::isPositive);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is negative.
	 * @return A boolean property based on whether the value of this property is negative.
	 * @see #isNegative()
	 */
	default BooleanProperty isNegativeProperty() {
		return derive(this::isNegative);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @return A boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @see #has(float)
	 */
	default BooleanProperty hasProperty(float value) {
		return derive(() -> has(value));
	}
	
	@Override
	default FixedFloatProperty snapshot() {
		return new FixedFloatProperty(get());
	}
	
	/**
	 * Checks if the value of this property is greater than the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is greater than the given value.
	 */
	default boolean isGreaterThan(float value) {
		return get() > value;
	}
	
	/**
	 * Checks if the value of this property is less than the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is less than the given value.
	 */
	default boolean isLessThan(float value) {
		return get() < value;
	}
	
	/**
	 * Checks if the value of this property is greater than or equal to the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is greater than or equal to the given value.
	 */
	default boolean isGreaterThanOrEqualTo(float value) {
		return get() >= value;
	}
	
	/**
	 * Checks if the value of this property is less than or equal to the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is less than or equal to the given value.
	 */
	default boolean isLessThanOrEqualTo(float value) {
		return get() <= value;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is greater than the given value.
	 * @see #compareWith(float)
	 */
	default BooleanProperty isGreaterThanProperty(float value) {
		return compareWith(value).isStrictlyPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is less than the given value.
	 * @see #compareWith(float)
	 */
	default BooleanProperty isLessThanProperty(float value) {
		return compareWith(value).isStrictlyNegativeProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than or equal to the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is greater than or equal to the given value.
	 * @see #compareWith(float)
	 */
	default BooleanProperty isGreaterThanOrEqualToProperty(float value) {
		return compareWith(value).isPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than or equal to the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is less than or equal to the given value.
	 * @see #compareWith(float)
	 */
	default BooleanProperty isLessThanOrEqualToProperty(float value) {
		return compareWith(value).isNegativeProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is greater than the value of the given property.
	 * @see #compareWith(FloatProperty)
	 */
	default BooleanProperty isGreaterThanProperty(FloatProperty other) {
		return compareWith(other).isStrictlyPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is less than the value of the given property.
	 * @see #compareWith(FloatProperty)
	 */
	default BooleanProperty isLessThanProperty(FloatProperty other) {
		return compareWith(other).isStrictlyNegativeProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than or equal to the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is greater than or equal to the value of the given property.
	 * @see #compareWith(FloatProperty)
	 */
	default BooleanProperty isGreaterThanOrEqualToProperty(FloatProperty other) {
		return compareWith(other).isPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than or equal to the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is less than or equal to the value of the given property.
	 * @see #compareWith(FloatProperty)
	 */
	default BooleanProperty isLessThanOrEqualToProperty(FloatProperty other) {
		return compareWith(other).isNegativeProperty();
	}
	
	/**
	 * @see #compareWith(FixedFloatProperty)
	 */
	default IntProperty compareWith(float value) {
		return compareWith(new FixedFloatProperty(value));
	}
	
	/**
	 * @see #compareWith(FloatProperty)
	 */
	default IntProperty compareWith(FixedFloatProperty other) {
		return compareWith((FloatProperty)other);
	}
	
	/**
	 * Compares this property with another property using the {@link #DEFAULT_ORDER}.
	 * @see #compareWith(FloatComparator, FloatProperty)
	 */
	default IntProperty compareWith(FloatProperty other) {
		return compareWith(DEFAULT_ORDER, other);
	}
	
	/**
	 * @see #compareWith(FloatComparator, FixedFloatProperty)
	 */
	default IntProperty compareWith(FloatComparator comparator, float value) {
		return compareWith(comparator, new FixedFloatProperty(value));
	}
	
	/**
	 * @see #compareWith(FloatComparator, FloatProperty)
	 */
	default IntProperty compareWith(FloatComparator comparator, FixedFloatProperty other) {
		return compareWith(comparator, (FloatProperty)other);
	}
	
	/**
	 * @see #compareWith(Comparator, Property)
	 */
	default IntProperty compareWith(FloatComparator comparator, FloatProperty other) {
		return new ComparedFloatProperty(this, comparator, other);
	}
	
	@Override
	default IntProperty hashCodeProperty() {
		return mapToInt(Float::hashCode);
	}
	
	default FloatProperty subtract(float operand) {
		return add(-operand);
	}
	
	default FloatProperty add(float operand) {
		return mapToFloat((value) -> value + operand);
	}
	
	default FloatProperty divide(float operand) {
		return multiply(1f / operand);
	}
	
	default FloatProperty multiply(float operand) {
		return mapToFloat((value) -> value * operand);
	}
	
	default IntProperty round() {
		return mapToInt(Math::round);
	}
	
	default IntProperty ceil() {
		return mapToInt((value) -> (int)Math.ceil(value));
	}
	
	default IntProperty floor() {
		return mapToInt((value) -> (int)Math.floor(value));
	}
	
	default FloatProperty mod(float operand) {
		return mapToFloat((value) -> value % operand);
	}
	
	/**
	 * @see #mapToBoolean(ToBooleanMapper) 
	 */
	default BooleanProperty mapToBoolean(FloatToBooleanMapper mapper) {
		return derive(() -> mapper.mapToBoolean(get()));
	}
	
	/**
	 * @see #mapToFloat(ToFloatMapper) 
	 */
	default FloatProperty mapToFloat(FloatMapper mapper) {
		return derive(() -> mapper.mapToFloat(get()));
	}
	
	/**
	 * @see #mapToInt(ToIntMapper) 
	 */
	default IntProperty mapToInt(FloatToIntMapper mapper) {
		return derive(() -> mapper.mapToInt(get()));
	}
	
	/**
	 * @see #mapToString(Mapper) 
	 */
	default StringProperty mapToString(FromFloatMapper<String> mapper) {
		return derive(() -> mapper.map(get()));
	}
	
	/**
	 * @see #map(Mapper)
	 */
	default <S> Property<S> map(FromFloatMapper<S> mapper) {
		return derive(() -> mapper.map(get()));
	}
	
	/**
	 * @see #fromProperty(Property)
	 */
	static FloatProperty fromProperty(FloatProperty property) {
		return property;
	}
	
	/**
	 * Converts a property to a float property.
	 * @param property The property to convert
	 * @return The converted float property.
	 */
	@Contract("_ -> new")
	static FloatProperty fromProperty(Property<Float> property) {
		return property::getValue;
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property is {@code null}.
	 * @return The value
	 */
	@Contract("null, _ -> param2")
	static float getValueOf(FloatProperty property, float defaultValue) {
		if (property == null) {
			return defaultValue;
		} else {
			return property.get();
		}
	}
	
}
