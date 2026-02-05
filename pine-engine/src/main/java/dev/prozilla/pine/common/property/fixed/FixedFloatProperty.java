package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.IntProperty;
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
	public IntProperty compareWith(FloatComparator comparator, FloatProperty other) {
		return other.compareWith(comparator, this).mapToInt((value) -> -value);
	}
	
	@Override
	public IntProperty compareWith(FloatComparator comparator, FixedFloatProperty other) {
		return new FixedIntProperty(comparator.compare(get(), other.get()));
	}
	
	@Contract("_ -> this")
	@Override
	public FixedFloatProperty replaceNull(Float defaultValue) {
		return this;
	}
	
}
