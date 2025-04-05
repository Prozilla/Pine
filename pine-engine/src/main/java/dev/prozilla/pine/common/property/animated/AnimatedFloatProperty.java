package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.easing.EasingFunction;

public class AnimatedFloatProperty extends AnimatedProperty<Float> {
	
	public AnimatedFloatProperty(Float start, Float end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedFloatProperty(Float start, Float end, float duration, EasingFunction easingFunction) {
		this(start, end, duration, easingFunction, DEFAULT_DIRECTION);
	}
	
	public AnimatedFloatProperty(Float start, Float end, float duration, EasingFunction easingFunction, AnimationDirection direction) {
		super(start, end, duration, easingFunction, direction);
	}
	
	@Override
	public Float getValue() {
		return MathUtils.remap(getFactor(), 0f, 1f, start, end);
	}
	
}
