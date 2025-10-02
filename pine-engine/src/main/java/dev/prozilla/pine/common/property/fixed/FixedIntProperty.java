package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.IntProperty;

public class FixedIntProperty implements FixedProperty<Integer>, IntProperty {
	
	protected final int value;
	
	public FixedIntProperty(int value) {
		this.value = value;
	}
	
	@Override
	public int get() {
		return value;
	}
	
	@Override
	public FixedBooleanProperty existsProperty() {
		return BooleanProperty.TRUE;
	}
	
}
