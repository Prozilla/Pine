package dev.prozilla.pine.common.property.animated.variable;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.animated.AnimatedProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;

public abstract class VariableAnimatedProperty<T> implements Property<AnimatedProperty<T>> {
	
	protected final Property<T> startProperty;
	protected final Property<T> endProperty;
	protected final Property<AnimationCurve> curveProperty;
	
	public VariableAnimatedProperty(Property<T> startProperty, Property<T> endProperty, Property<AnimationCurve> curveProperty) {
		this.startProperty = startProperty;
		this.endProperty = endProperty;
		this.curveProperty = curveProperty;
	}
}
