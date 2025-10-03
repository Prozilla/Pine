package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;

public abstract class FixedBooleanProperty implements FixedProperty<Boolean>, BooleanProperty {
	
	protected FixedBooleanProperty() {}
	
	@Override
	abstract public FixedBooleanProperty not();
	
	/**
	 * Returns a boolean property whose value is always {@code true};
	 * @return {@link BooleanProperty#TRUE}
	 */
	@Override
	public FixedBooleanProperty existenceProperty() {
		return BooleanProperty.super.existenceProperty();
	}
	
}
