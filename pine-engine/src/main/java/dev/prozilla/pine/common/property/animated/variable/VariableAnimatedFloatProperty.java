package dev.prozilla.pine.common.property.animated.variable;

import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.animated.AnimatedFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;

public class VariableAnimatedFloatProperty extends VariableAnimatedProperty<Float, FloatProperty> {
	
	public VariableAnimatedFloatProperty(FloatProperty startProperty, FloatProperty endProperty, AnimationCurve curve) {
		this(startProperty, endProperty, new FixedObjectProperty<>(curve));
	}
	
	public VariableAnimatedFloatProperty(FloatProperty startProperty, FloatProperty endProperty, Property<AnimationCurve> curveProperty) {
		super(startProperty, endProperty, curveProperty);
	}
	
	@Override
	public AnimatedFloatProperty getValue() {
		return new AnimatedFloatProperty(startProperty.get(), endProperty.get(), curveProperty.getValue());
	}
	
}
