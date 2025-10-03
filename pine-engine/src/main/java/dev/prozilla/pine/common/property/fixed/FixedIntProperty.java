package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.IntProperty;

public class FixedIntProperty implements FixedProperty<Integer>, IntProperty {
	
	protected final int value;
	
	public FixedIntProperty(int value) {
		this.value = value;
	}
	
	@Override
	public final int get() {
		return value;
	}
	
	@Override
	public FixedBooleanProperty existenceProperty() {
		return BooleanProperty.TRUE;
	}
	
	@Override
	public FixedBooleanProperty isZeroProperty() {
		return BooleanProperty.fromValue(get() == 0);
	}
}
