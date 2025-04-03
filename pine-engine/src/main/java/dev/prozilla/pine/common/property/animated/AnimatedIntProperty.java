package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;

public class AnimatedIntProperty extends AnimatedProperty<Integer> {
	
	public AnimatedIntProperty(Integer start, Integer end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedIntProperty(Integer start, Integer end, float duration, EasingFunction easingFunction) {
		super(start, end, duration, easingFunction);
	}
	
	@Override
	public Integer getValue() {
		return Math.round(easingFunction.get(time / duration, start, end));
	}
	
}
