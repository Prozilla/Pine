package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.IntProperty;

public class AnimatedIntProperty extends AnimatedProperty<Integer> implements IntProperty {
	
	protected int start;
	protected int end;
	
	public AnimatedIntProperty(int start, int end, AnimationCurve curve) {
		super(curve);
		this.start = start;
		this.end = end;
	}
	
	@Override
	public int get() {
		return Math.round(MathUtils.remap(getFactor(), 0f, 1f, start, end));
	}
	
}
