package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class VariableAnimatedColorProperty extends VariableAnimatedProperty<Color> {
	
	public VariableAnimatedColorProperty(VariableProperty<Color> startProperty, VariableProperty<Color> endProperty, VariableProperty<Float> durationProperty, VariableProperty<EasingFunction> easingFunctionProperty, VariableProperty<AnimationDirection> directionProperty) {
		super(startProperty, endProperty, durationProperty, easingFunctionProperty, directionProperty);
	}
	
	@Override
	public AnimatedColorProperty getValue() {
		return new AnimatedColorProperty(startProperty.getValue(), endProperty.getValue(), durationProperty.getValue(), easingFunctionProperty.getValue(), directionProperty.getValue());
	}
}
