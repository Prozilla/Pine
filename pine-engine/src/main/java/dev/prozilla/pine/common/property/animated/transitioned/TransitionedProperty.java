package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.property.animated.AnimatedProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;

import java.util.Objects;

public abstract class TransitionedProperty<T> extends AnimatedProperty<T> {
	
	public TransitionedProperty(T initialValue, AnimationCurve curve) {
		super(initialValue, initialValue, curve);
		transitionTo(initialValue);
	}
	
	public void transitionTo(T targetValue) {
		Objects.requireNonNull(targetValue, "target must not be null");
		
		if (end.equals(targetValue)) {
			return;
		}
		
		start = computeStart();
		end = targetValue;
	}
	
	protected T computeStart() {
		return getValue();
	}
}
