package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.property.VariableProperty;

public abstract class VariableAnimatedProperty<T> extends VariableProperty<AnimatedProperty<T>> {
	
	protected final VariableProperty<T> startProperty;
	protected final VariableProperty<T> endProperty;
	protected final VariableProperty<AnimationCurve> curveProperty;
	
	public VariableAnimatedProperty(VariableProperty<T> startProperty, VariableProperty<T> endProperty, VariableProperty<AnimationCurve> curveProperty) {
		this.startProperty = startProperty;
		this.endProperty = endProperty;
		this.curveProperty = curveProperty;
	}
}
