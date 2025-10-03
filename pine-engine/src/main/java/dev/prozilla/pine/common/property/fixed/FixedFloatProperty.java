package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.FloatProperty;

public class FixedFloatProperty implements FixedProperty<Float>, FloatProperty {
	
	protected final float value;
	
	public FixedFloatProperty(float value) {
		this.value = value;
	}
	
	@Override
	public final float get() {
		return value;
	}
	
	@Override
	public FixedBooleanProperty existenceProperty() {
		return BooleanProperty.TRUE;
	}
	
}
