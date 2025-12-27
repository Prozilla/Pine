package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.compared.ComparedFloatProperty;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.common.util.function.comparator.FloatComparator;
import dev.prozilla.pine.common.util.function.mapper.FloatMapper;
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
	
	default FloatProperty map(FloatMapper mapper) {
		return () -> mapper.map(get());
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
		return this::isStrictlyPositive;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is strictly negative.
	 * @return A boolean property based on whether the value of this property is strictly negative.
	 * @see #isStrictlyNegative()
	 */
	default BooleanProperty isStrictlyNegativeProperty() {
		return this::isStrictlyNegative;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is positive.
	 * @return A boolean property based on whether the value of this property is positive.
	 * @see #isPositive()
	 */
	default BooleanProperty isPositiveProperty() {
		return this::isPositive;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is negative.
	 * @return A boolean property based on whether the value of this property is negative.
	 * @see #isNegative()
	 */
	default BooleanProperty isNegativeProperty() {
		return this::isNegative;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @return A boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @see #has(float)
	 */
	default BooleanProperty hasProperty(float value) {
		return () -> has(value);
	}
	
	default boolean isGreaterThan(float value) {
		return get() > value;
	}
	
	default boolean isLessThan(float value) {
		return get() < value;
	}
	
	default boolean isGreaterThanOrEqualTo(float value) {
		return get() >= value;
	}
	
	default boolean isLessThanOrEqualTo(float value) {
		return get() <= value;
	}
	
	default BooleanProperty isGreaterThanProperty(float value) {
		return compareWith(value).isStrictlyPositiveProperty();
	}
	
	default BooleanProperty isLessThanProperty(float value) {
		return compareWith(value).isStrictlyNegativeProperty();
	}
	
	default BooleanProperty isGreaterThanOrEqualToProperty(float value) {
		return compareWith(value).isPositiveProperty();
	}
	
	default BooleanProperty isLessThanOrEqualToProperty(float value) {
		return compareWith(value).isNegativeProperty();
	}
	
	default BooleanProperty isGreaterThanProperty(FloatProperty other) {
		return compareWith(other).isStrictlyPositiveProperty();
	}
	
	default BooleanProperty isLessThanProperty(FloatProperty other) {
		return compareWith(other).isStrictlyNegativeProperty();
	}
	
	default BooleanProperty isGreaterThanOrEqualToProperty(FloatProperty other) {
		return compareWith(other).isPositiveProperty();
	}
	
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
	
	static FloatProperty fromProperty(FloatProperty property) {
		return property;
	}
	
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
