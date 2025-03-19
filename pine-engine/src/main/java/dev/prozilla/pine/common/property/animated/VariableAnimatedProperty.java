package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.Easing;
import dev.prozilla.pine.common.property.VariableProperty;

public abstract class VariableAnimatedProperty<T> extends VariableProperty<AnimatedProperty<T>> {
	
	protected final VariableProperty<T> start;
	protected final VariableProperty<T> end;
	protected final VariableProperty<Float> duration;
	protected final VariableProperty<Easing> easing;
	
	public VariableAnimatedProperty(VariableProperty<T> start, VariableProperty<T> end, VariableProperty<Float> duration, VariableProperty<Easing> easing) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		this.easing = easing;
	}
}
