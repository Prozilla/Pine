package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.property.FixedProperty;
import dev.prozilla.pine.common.property.VariableProperty;

public class VariableAnimatedFloatProperty extends VariableAnimatedProperty<Float> {
	
	public VariableAnimatedFloatProperty(VariableProperty<Float> startProperty, VariableProperty<Float> endProperty, AnimationCurve curve) {
		this(startProperty, endProperty, new FixedProperty<>(curve));
	}
	
	public VariableAnimatedFloatProperty(VariableProperty<Float> startProperty, VariableProperty<Float> endProperty, VariableProperty<AnimationCurve> curveProperty) {
		super(startProperty, endProperty, curveProperty);
	}
	
	@Override
	public AnimatedFloatProperty getValue() {
		return new AnimatedFloatProperty(startProperty.getValue(), endProperty.getValue(), curveProperty.getValue());
	}
}
