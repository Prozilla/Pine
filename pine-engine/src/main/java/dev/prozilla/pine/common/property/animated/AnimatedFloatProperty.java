package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;

public class AnimatedFloatProperty extends AnimatedProperty<Float> {
	
	public AnimatedFloatProperty(Float start, Float end, AnimationCurve curve) {
		super(start, end, curve);
	}
	
	@Override
	public Float getValue() {
		return MathUtils.remap(getFactor(), 0f, 1f, start, end);
	}
	
}
