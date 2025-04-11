package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;

public class AnimatedIntProperty extends AnimatedProperty<Integer> {
	
	public AnimatedIntProperty(Integer start, Integer end, AnimationCurve curve) {
		super(start, end, curve);
	}
	
	@Override
	public Integer getValue() {
		return Math.round(MathUtils.remap(getFactor(), 0f, 1f, start, end));
	}
	
}
