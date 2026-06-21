package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.compared.ComparedIntProperty;
import dev.prozilla.pine.common.property.fixed.FixedIntProperty;
import dev.prozilla.pine.common.util.function.comparator.IntComparator;
import dev.prozilla.pine.common.util.function.mapper.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * A property with an integer value.
 */
@FunctionalInterface
public interface IntProperty extends NonNullProperty<Integer> {
	
	IntComparator DEFAULT_ORDER = IntComparator.naturalOrder();
	
	@Override
	default Integer getValueOr(Integer defaultValue) {
		return getValue();
	}
	
	@Override
	@NotNull
	default Integer requireValue() {
		return getValue();
	}
	
	@Override
	default Integer getValue() {
		return get();
	}
	
	/**
	 * Returns the primitive value of this property.
	 * @return The primitive value of this property.
	 */
	int get();
	
	/**
	 * @see #hasValue(Object)
	 */
	default boolean has(int value) {
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
	default IntProperty replaceNull(Integer defaultValue) {
		return this;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is {@code 0}.
	 * @return A boolean property based on whether the value of this property is {@code 0}.
	 * @see #hasProperty(int)
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
	 * @see #has(int)
	 */
	default BooleanProperty hasProperty(int value) {
		return derive(() -> has(value));
	}
	
	@Override
	default FixedIntProperty snapshot() {
		return new FixedIntProperty(get());
	}
	
	/**
	 * Checks if the value of this property is greater than the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is greater than the given value.
	 */
	default boolean isGreaterThan(int value) {
		return get() > value;
	}
	
	/**
	 * Checks if the value of this property is less than the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is less than the given value.
	 */
	default boolean isLessThan(int value) {
		return get() < value;
	}
	
	/**
	 * Checks if the value of this property is greater than or equal to the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is greater than or equal to the given value.
	 */
	default boolean isGreaterThanOrEqualTo(int value) {
		return get() >= value;
	}
	
	/**
	 * Checks if the value of this property is less than or equal to the given value.
	 * @param value The value to compare with
	 * @return {@code true} if the value of this property is less than or equal to the given value.
	 */
	default boolean isLessThanOrEqualTo(int value) {
		return get() <= value;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is greater than the given value.
	 * @see #compareWith(int)
	 */
	default BooleanProperty isGreaterThanProperty(int value) {
		return compareWith(value).isStrictlyPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is less than the given value.
	 * @see #compareWith(int)
	 */
	default BooleanProperty isLessThanProperty(int value) {
		return compareWith(value).isStrictlyNegativeProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than or equal to the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is greater than or equal to the given value.
	 * @see #compareWith(int)
	 */
	default BooleanProperty isGreaterThanOrEqualToProperty(int value) {
		return compareWith(value).isPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than or equal to the given value.
	 * @param value The value to compare with
	 * @return A boolean property based on whether the value of this property is less than or equal to the given value.
	 * @see #compareWith(int)
	 */
	default BooleanProperty isLessThanOrEqualToProperty(int value) {
		return compareWith(value).isNegativeProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is greater than the value of the given property.
	 * @see #compareWith(IntProperty)
	 */
	default BooleanProperty isGreaterThanProperty(IntProperty other) {
		return compareWith(other).isStrictlyPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is less than the value of the given property.
	 * @see #compareWith(IntProperty)
	 */
	default BooleanProperty isLessThanProperty(IntProperty other) {
		return compareWith(other).isStrictlyNegativeProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is greater than or equal to the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is greater than or equal to the value of the given property.
	 * @see #compareWith(IntProperty)
	 */
	default BooleanProperty isGreaterThanOrEqualToProperty(IntProperty other) {
		return compareWith(other).isPositiveProperty();
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is less than or equal to the value of the given property.
	 * @param other The property to compare with
	 * @return A boolean property based on whether the value of this property is less than or equal to the value of the given property.
	 * @see #compareWith(IntProperty)
	 */
	default BooleanProperty isLessThanOrEqualToProperty(IntProperty other) {
		return compareWith(other).isNegativeProperty();
	}
	
	/**
	 * @see #compareWith(FixedIntProperty)
	 */
	default IntProperty compareWith(int value) {
		return compareWith(new FixedIntProperty(value));
	}
	
	/**
	 * @see #compareWith(IntProperty)
	 */
	default IntProperty compareWith(FixedIntProperty other) {
		return compareWith((IntProperty)other);
	}
	
	/**
	 * Compares this property with another property using the {@link #DEFAULT_ORDER}.
	 * @see #compareWith(IntComparator, IntProperty)
	 */
	default IntProperty compareWith(IntProperty other) {
		return compareWith(DEFAULT_ORDER, other);
	}
	
	/**
	 * @see #compareWith(IntComparator, FixedIntProperty)
	 */
	default IntProperty compareWith(IntComparator comparator, int value) {
		return compareWith(comparator, new FixedIntProperty(value));
	}
	
	/**
	 * @see #compareWith(IntComparator, IntProperty)
	 */
	default IntProperty compareWith(IntComparator comparator, FixedIntProperty other) {
		return compareWith(comparator, (IntProperty)other);
	}
	
	/**
	 * @see #compareWith(Comparator, Property)
	 */
	default IntProperty compareWith(IntComparator comparator, IntProperty other) {
		return new ComparedIntProperty(this, comparator, other);
	}
	
	@Contract("-> this")
	@Override
	default IntProperty hashCodeProperty() {
		// The hash code of an integer is the integer itself
		return this;
	}
	
	default IntProperty subtract(int operand) {
		return add(-operand);
	}
	
	default IntProperty add(int operand) {
		return mapToInt((value) -> value + operand);
	}
	
	default FloatProperty divide(float operand) {
		return multiply(1f / operand);
	}
	
	default IntProperty divide(int operand) {
		return mapToInt((value) -> value / operand);
	}
	
	default FloatProperty multiply(float operand) {
		return mapToFloat((value) -> value * operand);
	}
	
	default IntProperty multiply(int operand) {
		return mapToInt((value) -> value * operand);
	}
	
	/**
	 * @see #mapToBoolean(ToBooleanMapper)
	 */
	default BooleanProperty mapToBoolean(IntToBooleanMapper mapper) {
		return derive(() -> mapper.mapToBoolean(get()));
	}
	
	/**
	 * @see #mapToFloat(ToFloatMapper)
	 */
	default FloatProperty mapToFloat(IntToFloatMapper mapper) {
		return derive(() -> mapper.mapToFloat(get()));
	}
	
	/**
	 * @see #mapToInt(ToIntMapper)
	 */
	default IntProperty mapToInt(IntMapper mapper) {
		return derive(() -> mapper.mapToInt(get()));
	}
	
	/**
	 * @see #mapToString(Mapper)
	 */
	default StringProperty mapToString(FromIntMapper<String> mapper) {
		return derive(() -> mapper.map(get()));
	}
	
	/**
	 * @see #map(Mapper)
	 */
	default <S> Property<S> map(FromIntMapper<S> mapper) {
		return derive(() -> mapper.map(get()));
	}
	
	/**
	 * @see #fromProperty(Property)
	 */
	static IntProperty fromProperty(IntProperty property) {
		return property;
	}
	
	/**
	 * Converts a property to an integer property.
	 * @param property The property to convert
	 * @return The converted integer property.
	 */
	@Contract("_ -> new")
	static IntProperty fromProperty(Property<Integer> property) {
		return property::getValue;
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property is {@code null}.
	 * @return The value
	 */
	@Contract("null, _ -> param2")
	static int getValueOf(IntProperty property, int defaultValue) {
		if (property == null) {
			return defaultValue;
		} else {
			return property.get();
		}
	}
	
}
