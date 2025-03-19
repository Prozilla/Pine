package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.VariableProperty;

public class VariableAnimatedFloatProperty extends VariableAnimatedProperty<Float> {
	
	public VariableAnimatedFloatProperty(VariableProperty<Float> start, VariableProperty<Float> end, VariableProperty<Float> duration, VariableProperty<EasingFunction> easingFunction) {
		super(start, end, duration, easingFunction);
	}
	
	@Override
	public AnimatedFloatProperty getValue() {
		return new AnimatedFloatProperty(start.getValue(), end.getValue(), duration.getValue(), easingFunction.getValue());
	}
}
