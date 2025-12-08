package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.property.animated.AnimatedIntProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.common.util.checks.Checks;

public class TransitionedIntProperty extends AnimatedIntProperty implements TransitionedProperty<Integer> {
	
	public TransitionedIntProperty(int initialValue, AnimationCurve curve) {
		super(initialValue, initialValue, curve);
		transitionTo(initialValue);
	}
	
	@Override
	public void transitionToValue(Integer targetValue) {
		transitionTo(ObjectUtils.unbox(targetValue));
	}
	
	public void transitionTo(int targetValue) {
		Checks.isNotNull(targetValue, "targetValue");
		
		if (end == targetValue) {
			return;
		}
		
		start = hasFinished() ? end : computeStart();
		end = targetValue;
		
		restart();
	}
	
	protected int computeStart() {
		return get();
	}
	
}
