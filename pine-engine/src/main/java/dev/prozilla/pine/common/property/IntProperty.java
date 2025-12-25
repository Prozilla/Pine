package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.compared.ComparedIntProperty;
import dev.prozilla.pine.common.property.fixed.FixedIntProperty;
import dev.prozilla.pine.common.util.function.comparator.IntComparator;
import dev.prozilla.pine.common.util.function.mapper.IntMapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A property with an integer value.
 */
@FunctionalInterface
public interface IntProperty extends NonNullProperty<Integer> {
	
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
	
	default IntProperty map(IntMapper mapper) {
		return () -> mapper.map(get());
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
	 * @see #has(int)
	 */
	default BooleanProperty hasProperty(int value) {
		return () -> has(value);
	}
	
	default boolean isGreaterThan(int value) {
		return get() > value;
	}
	
	default boolean isLessThan(int value) {
		return get() < value;
	}
	
	default boolean isGreaterThanOrEqualTo(int value) {
		return get() >= value;
	}
	
	default boolean isLessThanOrEqualTo(int value) {
		return get() <= value;
	}
	
	default BooleanProperty isGreaterThanProperty(int value) {
		return compareWith(value).isStrictlyPositiveProperty();
	}
	
	default BooleanProperty isLessThanProperty(int value) {
		return compareWith(value).isStrictlyNegativeProperty();
	}
	
	default BooleanProperty isGreaterThanOrEqualToProperty(int value) {
		return compareWith(value).isPositiveProperty();
	}
	
	default BooleanProperty isLessThanOrEqualToProperty(int value) {
		return compareWith(value).isNegativeProperty();
	}
	
	default BooleanProperty isGreaterThanProperty(IntProperty other) {
		return compareWith(other).isStrictlyPositiveProperty();
	}
	
	default BooleanProperty isLessThanProperty(IntProperty other) {
		return compareWith(other).isStrictlyNegativeProperty();
	}
	
	default BooleanProperty isGreaterThanOrEqualToProperty(IntProperty other) {
		return compareWith(other).isPositiveProperty();
	}
	
	default BooleanProperty isLessThanOrEqualToProperty(IntProperty other) {
		return compareWith(other).isNegativeProperty();
	}
	
	default IntProperty compareWith(int value) {
		return compareWith(new FixedIntProperty(value));
	}
	
	default IntProperty compareWith(IntProperty other) {
		return compareWith(IntComparator.naturalOrder(), other);
	}
	
	default IntProperty compareWith(IntComparator comparator, int value) {
		return compareWith(comparator, new FixedIntProperty(value));
	}
	
	default IntProperty compareWith(IntComparator comparator, IntProperty other) {
		return new ComparedIntProperty(this, comparator, other);
	}
	
	@Contract("-> this")
	@Override
	default IntProperty hashCodeProperty() {
		// The hash code of an integer is the integer itself
		return this;
	}
	
	static IntProperty fromProperty(IntProperty property) {
		return property;
	}
	
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
