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
	public IntProperty compareWith(IntComparator comparator, IntProperty other) {
		return other.compareWith(comparator, this).mapToInt((value) -> -value);
	}
	
	@Override
	public IntProperty compareWith(IntComparator comparator, FixedIntProperty other) {
		return new FixedIntProperty(comparator.compare(get(), other.get()));
	}
	
	@Contract("_ -> this")
	@Override
	public FixedIntProperty replaceNull(Integer defaultValue) {
		return this;
	}
	
}
