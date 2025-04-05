package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.property.FixedProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class VariableAnimatedColorProperty extends VariableAnimatedProperty<Color> {
	
	public VariableAnimatedColorProperty(VariableProperty<Color> startProperty, VariableProperty<Color> endProperty, AnimationCurve curve) {
		this(startProperty, endProperty, new FixedProperty<>(curve));
	}
	
	public VariableAnimatedColorProperty(VariableProperty<Color> startProperty, VariableProperty<Color> endProperty, VariableProperty<AnimationCurve> curveProperty) {
		super(startProperty, endProperty, curveProperty);
	}
	
	@Override
	public AnimatedColorProperty getValue() {
		return new AnimatedColorProperty(startProperty.getValue(), endProperty.getValue(), curveProperty.getValue());
	}
	
}
