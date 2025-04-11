package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.animated.AnimationCurve;

public class TransitionedIntProperty extends TransitionedProperty<Integer> {
	
	public TransitionedIntProperty(Integer initialValue, AnimationCurve curve) {
		super(initialValue, curve);
	}
	
	@Override
	public Integer getValue() {
		return Math.round(MathUtils.remap(getFactor(), 0f, 1f, start, end));
	}
	
}
