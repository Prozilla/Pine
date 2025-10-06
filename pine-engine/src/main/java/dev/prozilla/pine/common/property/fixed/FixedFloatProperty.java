package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.FloatProperty;
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
	
	@Contract("_ -> this")
	@Override
	public FixedFloatProperty replaceNull(Float defaultValue) {
		return this;
	}
	
}
