package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.util.function.comparator.FloatComparator;
import org.jetbrains.annotations.Contract;

public class FixedFloatProperty implements FloatProperty, FixedProperty<Float> {
	
	protected final float value;
	
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
	
	public FixedIntProperty compareWith(FixedFloatProperty other) {
		return compareWith(FloatComparator.naturalOrder(), other);
	}
	
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
