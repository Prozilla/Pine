package dev.prozilla.pine.common.property.animated.variable;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.animated.AnimatedColorProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
import dev.prozilla.pine.common.system.Color;

public class VariableAnimatedColorProperty extends VariableAnimatedProperty<Color, Property<Color>> {
	
	public VariableAnimatedColorProperty(Property<Color> startProperty, Property<Color> endProperty, AnimationCurve curve) {
		this(startProperty, endProperty, new FixedObjectProperty<>(curve));
	}
	
	public VariableAnimatedColorProperty(Property<Color> startProperty, Property<Color> endProperty, Property<AnimationCurve> curveProperty) {
		super(startProperty, endProperty, curveProperty);
	}
	
	@Override
	public AnimatedColorProperty getValue() {
		return new AnimatedColorProperty(startProperty.getValue(), endProperty.getValue(), curveProperty.getValue());
	}
	
}
