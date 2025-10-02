package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;

public abstract class FixedBooleanProperty implements FixedProperty<Boolean>, BooleanProperty {
	
	protected FixedBooleanProperty() {
	
	}
	
	@Override
	abstract public FixedBooleanProperty not();
	
}
