package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.VariableProperty;

public class VariableAnimatedFloatProperty extends VariableAnimatedProperty<Float> {
	
	public VariableAnimatedFloatProperty(VariableProperty<Float> startProperty, VariableProperty<Float> endProperty, VariableProperty<Float> durationProperty, VariableProperty<EasingFunction> easingFunctionProperty, VariableProperty<AnimationDirection> directionProperty) {
		super(startProperty, endProperty, durationProperty, easingFunctionProperty, directionProperty);
	}
	
	@Override
	public AnimatedFloatProperty getValue() {
		return new AnimatedFloatProperty(startProperty.getValue(), endProperty.getValue(), durationProperty.getValue(), easingFunctionProperty.getValue(), directionProperty.getValue());
	}
}
