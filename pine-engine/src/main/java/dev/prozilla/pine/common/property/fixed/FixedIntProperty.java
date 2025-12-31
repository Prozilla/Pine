package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.util.function.comparator.IntComparator;
import org.jetbrains.annotations.Contract;

/**
 * A property with an integer value that never changes.
 */
public class FixedIntProperty implements IntProperty, FixedProperty<Integer> {
	
	protected final int value;
	
	/**
	 * Creates a fixed property with the given value.
	 */
	public FixedIntProperty(int value) {
		this.value = value;
	}
	
	@Override
	public final int get() {
		return value;
	}
	
	@Override
	public FixedBooleanProperty isNotNullProperty() {
		return BooleanProperty.TRUE;
	}
	
	@Contract("-> this")
	@Override
	public FixedIntProperty snapshot() {
		return this;
	}
	
	@Override
	public FixedBooleanProperty isGreaterThanProperty(int value) {
		return BooleanProperty.fromValue(isGreaterThan(value));
	}
	
	@Override
	public FixedBooleanProperty isLessThanProperty(int value) {
		return BooleanProperty.fromValue(isLessThan(value));
	}
	
	@Override
	public FixedBooleanProperty isGreaterThanOrEqualToProperty(int value) {
		return BooleanProperty.fromValue(isGreaterThanOrEqualTo(value));
	}
	
	@Override
	public FixedBooleanProperty isLessThanOrEqualToProperty(int value) {
		return BooleanProperty.fromValue(isLessThanOrEqualTo(value));
	}
	
	@Override
	public BooleanProperty isGreaterThanProperty(IntProperty other) {
		return other.compareWith(get()).isStrictlyPositiveProperty();
	}
	
	@Override
	public BooleanProperty isLessThanProperty(IntProperty other) {
		return other.compareWith(get()).isStrictlyNegativeProperty();
	}
	
	@Override
	public BooleanProperty isGreaterThanOrEqualToProperty(IntProperty other) {
		return other.compareWith(get()).isPositiveProperty();
	}
	
	@Override
	public BooleanProperty isLessThanOrEqualToProperty(IntProperty other) {
		return other.compareWith(get()).isNegativeProperty();
	}
	
	@Override
	public FixedIntProperty compareWith(int value) {
		return compareWith(IntProperty.DEFAULT_ORDER, value);
	}
	
	@Override
	public FixedIntProperty compareWith(FixedIntProperty other) {
		return compareWith(IntProperty.DEFAULT_ORDER, other);
	}
	
	@Override
	public FixedIntProperty compareWith(IntComparator comparator, int value) {
		return new FixedIntProperty(comparator.compare(get(), value));
	}
	
	@Override
	public FixedIntProperty compareWith(IntComparator comparator, FixedIntProperty other) {
		return new FixedIntProperty(comparator.compare(get(), other.get()));
	}
	
	@Contract("-> this")
	@Override
	public FixedIntProperty hashCodeProperty() {
		// The hash code of an integer is the integer itself
		return this;
	}
	
	@Override
	public FixedBooleanProperty isZeroProperty() {
		return BooleanProperty.fromValue(has(0));
	}
	
	@Override
	public FixedBooleanProperty isStrictlyPositiveProperty() {
		return BooleanProperty.fromValue(isStrictlyPositive());
	}
	
	@Override
	public FixedBooleanProperty isStrictlyNegativeProperty() {
		return BooleanProperty.fromValue(isStrictlyNegative());
	}
	
	@Override
	public FixedBooleanProperty isPositiveProperty() {
		return BooleanProperty.fromValue(isPositive());
	}
	
	@Override
	public FixedBooleanProperty isNegativeProperty() {
		return BooleanProperty.fromValue(isNegative());
	}
	
	@Contract("_ -> this")
	@Override
	public FixedIntProperty replaceNull(Integer defaultValue) {
		return this;
	}
	
	@Override
	public FixedBooleanProperty hasProperty(int value) {
		return BooleanProperty.fromValue(has(value));
	}
	
}
