package dev.prozilla.pine.common.property.animated.variable;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.animated.AnimatedProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;

public abstract class VariableAnimatedProperty<T, P extends Property<T>> implements Property<AnimatedProperty<T>> {
	
	protected final P startProperty;
	protected final P endProperty;
	protected final Property<AnimationCurve> curveProperty;
	
	public VariableAnimatedProperty(P startProperty, P endProperty, Property<AnimationCurve> curveProperty) {
		this.startProperty = startProperty;
		this.endProperty = endProperty;
		this.curveProperty = curveProperty;
	}
	
}
