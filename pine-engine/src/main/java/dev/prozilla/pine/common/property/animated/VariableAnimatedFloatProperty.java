package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.Easing;
import dev.prozilla.pine.common.property.VariableProperty;

public class VariableAnimatedFloatProperty extends VariableAnimatedProperty<Float> {
	
	public VariableAnimatedFloatProperty(VariableProperty<Float> start, VariableProperty<Float> end, VariableProperty<Float> duration, VariableProperty<Easing> easing) {
		super(start, end, duration, easing);
	}
	
	@Override
	public AnimatedFloatProperty getValue() {
		return new AnimatedFloatProperty(start.getValue(), end.getValue(), duration.getValue(), easing.getValue());
	}
}
