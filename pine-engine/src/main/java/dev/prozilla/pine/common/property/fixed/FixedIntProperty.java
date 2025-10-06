package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.IntProperty;
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
	
	@Override
	public FixedBooleanProperty isZeroProperty() {
		return BooleanProperty.fromValue(get() == 0);
	}
	
	@Contract("_ -> this")
	@Override
	public FixedIntProperty replaceNull(Integer defaultValue) {
		return this;
	}
	
}
