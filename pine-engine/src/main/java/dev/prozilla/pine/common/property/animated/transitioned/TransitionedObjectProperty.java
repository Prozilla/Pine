package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.property.animated.AnimatedObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.util.checks.Checks;

public abstract class TransitionedObjectProperty<T> extends AnimatedObjectProperty<T> implements TransitionedProperty<T> {
	
	public TransitionedObjectProperty(T initialValue, AnimationCurve curve) {
		super(initialValue, initialValue, curve);
		transitionToValue(initialValue);
	}
	
	@Override
	public void transitionToValue(T targetValue) {
		Checks.isNotNull(targetValue, "targetValue");
		
		if (end.equals(targetValue)) {
			return;
		}
		
		start = hasFinished() ? end : computeStartValue();
		end = targetValue;
		
		restart();
	}
	
	protected T computeStartValue() {
		return getValue();
	}

}
