package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.VariableProperty;

public abstract class VariableAnimatedProperty<T> extends VariableProperty<AnimatedProperty<T>> {
	
	protected final VariableProperty<T> startProperty;
	protected final VariableProperty<T> endProperty;
	protected final VariableProperty<Float> durationProperty;
	protected final VariableProperty<EasingFunction> easingFunctionProperty;
	protected final VariableProperty<AnimationDirection> directionProperty;
	
	public VariableAnimatedProperty(VariableProperty<T> startProperty, VariableProperty<T> endProperty, VariableProperty<Float> durationProperty, VariableProperty<EasingFunction> easingFunctionProperty, VariableProperty<AnimationDirection> directionProperty) {
		this.startProperty = startProperty;
		this.endProperty = endProperty;
		this.durationProperty = durationProperty;
		this.easingFunctionProperty = easingFunctionProperty;
		this.directionProperty = directionProperty;
	}
}
