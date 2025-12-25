package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.util.function.comparator.IntComparator;
import org.jetbrains.annotations.Contract;

public class FixedIntProperty implements IntProperty, FixedProperty<Integer> {
	
	protected final int value;
	
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
	
	public FixedIntProperty compareWith(FixedIntProperty other) {
		return compareWith(IntComparator.naturalOrder(), other);
	}
	
	public FixedIntProperty compareWith(IntComparator comparator, FixedIntProperty other) {
		return new FixedIntProperty(comparator.compare(get(), other.get()));
	}
	
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
