package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;

public class AnimatedFloatProperty extends AnimatedProperty<Float> {
	
	public AnimatedFloatProperty(Float start, Float end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedFloatProperty(Float start, Float end, float duration, EasingFunction easingFunction) {
		super(start, end, duration, easingFunction);
	}
	
	@Override
	public Float getValue() {
		return easingFunction.get(time / duration, start, end);
	}
	
}
