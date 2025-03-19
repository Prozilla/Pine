package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.VariableProperty;

public abstract class VariableAnimatedProperty<T> extends VariableProperty<AnimatedProperty<T>> {
	
	protected final VariableProperty<T> start;
	protected final VariableProperty<T> end;
	protected final VariableProperty<Float> duration;
	protected final VariableProperty<EasingFunction> easingFunction;
	
	public VariableAnimatedProperty(VariableProperty<T> start, VariableProperty<T> end, VariableProperty<Float> duration, VariableProperty<EasingFunction> easingFunction) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		this.easingFunction = easingFunction;
	}
}
