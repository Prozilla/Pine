package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.Easing;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class VariableAnimatedColorProperty extends VariableAnimatedProperty<Color> {
	
	public VariableAnimatedColorProperty(VariableProperty<Color> start, VariableProperty<Color> end, VariableProperty<Float> duration, VariableProperty<Easing> easing) {
		super(start, end, duration, easing);
	}
	
	@Override
	public AnimatedColorProperty getValue() {
		return new AnimatedColorProperty(start.getValue(), end.getValue(), duration.getValue(), easing.getValue());
	}
}
