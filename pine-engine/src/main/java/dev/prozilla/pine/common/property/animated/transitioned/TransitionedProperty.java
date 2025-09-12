package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.property.animated.AnimatedProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.util.checks.Checks;

public abstract class TransitionedProperty<T> extends AnimatedProperty<T> {
	
	public TransitionedProperty(T initialValue, AnimationCurve curve) {
		super(initialValue, initialValue, curve);
		transitionTo(initialValue);
	}
	
	public void transitionTo(T targetValue) {
		Checks.isNotNull(targetValue, "targetValue");
		
		if (end.equals(targetValue)) {
			return;
		}
		
		start = hasFinished() ? end : computeStart();
		end = targetValue;
		
		restart();
	}
	
	protected T computeStart() {
		return getValue();
	}
}
