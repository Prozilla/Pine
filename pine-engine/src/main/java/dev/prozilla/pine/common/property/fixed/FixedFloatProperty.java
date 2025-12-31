package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.util.function.comparator.FloatComparator;
import org.jetbrains.annotations.Contract;

/**
 * A property with a float value that never changes.
 */
public class FixedFloatProperty implements FloatProperty, FixedProperty<Float> {
	
	protected final float value;
	
	/**
	 * Creates a fixed property with the given value.
	 */
	public FixedFloatProperty(float value) {
		this.value = value;
	}
	
	@Override
	public final float get() {
		return value;
	}
	
	@Override
	public FixedBooleanProperty isNotNullProperty() {
		return BooleanProperty.TRUE;
	}
	
	@Contract("-> this")
	@Override
	public FixedFloatProperty snapshot() {
		return this;
	}
	
	@Override
	public FixedBooleanProperty isGreaterThanProperty(float value) {
		return BooleanProperty.fromValue(isGreaterThan(value));
	}
	
	@Override
	public FixedBooleanProperty isLessThanProperty(float value) {
		return BooleanProperty.fromValue(isLessThan(value));
	}
	
	@Override
	public FixedBooleanProperty isGreaterThanOrEqualToProperty(float value) {
		return BooleanProperty.fromValue(isGreaterThanOrEqualTo(value));
	}
	
	@Override
	public FixedBooleanProperty isLessThanOrEqualToProperty(float value) {
		return BooleanProperty.fromValue(isLessThanOrEqualTo(value));
	}
	
	@Override
	public BooleanProperty isGreaterThanProperty(FloatProperty other) {
		return other.compareWith(get()).isStrictlyPositiveProperty();
	}
	
	@Override
	public BooleanProperty isLessThanProperty(FloatProperty other) {
		return other.compareWith(get()).isStrictlyNegativeProperty();
	}
	
	@Override
	public BooleanProperty isGreaterThanOrEqualToProperty(FloatProperty other) {
		return other.compareWith(get()).isPositiveProperty();
	}
	
	@Override
	public BooleanProperty isLessThanOrEqualToProperty(FloatProperty other) {
		return other.compareWith(get()).isNegativeProperty();
	}
	
	@Override
	public FixedIntProperty compareWith(float value) {
		return compareWith(FloatProperty.DEFAULT_ORDER, value);
	}
	
	@Override
	public FixedIntProperty compareWith(FixedFloatProperty other) {
		return compareWith(FloatProperty.DEFAULT_ORDER, other);
	}
	
	@Override
	public FixedIntProperty compareWith(FloatComparator comparator, float value) {
		return new FixedIntProperty(comparator.compare(get(), value));
	}
	
	@Override
	public FixedIntProperty compareWith(FloatComparator comparator, FixedFloatProperty other) {
		return new FixedIntProperty(comparator.compare(get(), other.get()));
	}
	
	@Override
	public FixedIntProperty hashCodeProperty() {
		return new FixedIntProperty(Float.hashCode(get()));
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
	public FixedFloatProperty replaceNull(Float defaultValue) {
		return this;
	}
	
	@Override
	public FixedBooleanProperty hasProperty(float value) {
		return BooleanProperty.fromValue(has(value));
	}
	
}
