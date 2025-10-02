package dev.prozilla.pine.common.property.animated.variable;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.animated.AnimatedFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;

public class VariableAnimatedFloatProperty extends VariableAnimatedProperty<Float> {
	
	public VariableAnimatedFloatProperty(Property<Float> startProperty, Property<Float> endProperty, AnimationCurve curve) {
		this(startProperty, endProperty, new FixedObjectProperty<>(curve));
	}
	
	public VariableAnimatedFloatProperty(Property<Float> startProperty, Property<Float> endProperty, Property<AnimationCurve> curveProperty) {
		super(startProperty, endProperty, curveProperty);
	}
	
	@Override
	public AnimatedFloatProperty getValue() {
		return new AnimatedFloatProperty(startProperty.getValue(), endProperty.getValue(), curveProperty.getValue());
	}
}
