package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.property.animated.AnimatedFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.util.checks.Checks;

public class TransitionedFloatProperty extends AnimatedFloatProperty implements TransitionedProperty<Float> {
	
	public TransitionedFloatProperty(float initialValue, AnimationCurve curve) {
		super(initialValue, initialValue, curve);
		transitionTo(initialValue);
	}
	
	@Override
	public void transitionToValue(Float targetValue) {
		transitionTo(targetValue);
	}
	
	public void transitionTo(float targetValue) {
		Checks.isNotNull(targetValue, "targetValue");
		
		if (end == targetValue) {
			return;
		}
		
		start = hasFinished() ? end : computeStart();
		end = targetValue;
		
		restart();
	}
	
	protected float computeStart() {
		return get();
	}
	
}
