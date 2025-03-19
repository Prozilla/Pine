package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.Easing;

public class AnimatedFloatProperty extends AnimatedProperty<Float> {
	
	public AnimatedFloatProperty(Float start, Float end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING);
	}
	
	public AnimatedFloatProperty(Float start, Float end, float duration, Easing easing) {
		super(start, end, duration, easing);
	}
	
	@Override
	public Float getValue() {
		return easing.get(time / duration, start, end);
	}
	
}
