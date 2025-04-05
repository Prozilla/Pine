package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.easing.EasingFunction;

public class AnimatedIntProperty extends AnimatedProperty<Integer> {
	
	public AnimatedIntProperty(Integer start, Integer end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedIntProperty(Integer start, Integer end, float duration, EasingFunction easingFunction) {
		this(start, end, duration, easingFunction, DEFAULT_DIRECTION);
	}
	
	public AnimatedIntProperty(Integer start, Integer end, float duration, EasingFunction easingFunction, AnimationDirection direction) {
		super(start, end, duration, easingFunction, direction);
	}
	
	@Override
	public Integer getValue() {
		return Math.round(MathUtils.remap(getFactor(), 0f, 1f, start, end));
	}
	
}
