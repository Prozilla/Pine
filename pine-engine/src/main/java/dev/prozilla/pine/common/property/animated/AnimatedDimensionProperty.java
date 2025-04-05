package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.easing.EasingFunction;

public class AnimatedDimensionProperty extends AnimatedProperty<DimensionBase> {
	
	protected final Dimension.Mix mixedDimension;
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, float duration) {
		this(start, end, duration, DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, float duration, EasingFunction easingFunction) {
		this(start, end, duration, easingFunction, DEFAULT_DIRECTION);
	}
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, float duration, EasingFunction easingFunction, AnimationDirection direction) {
		super(start, end, duration, easingFunction, direction);
		mixedDimension = new Dimension.Mix(start, end);
	}
	
	@Override
	public DimensionBase getValue() {
		mixedDimension.setFactor(getFactor());
		return mixedDimension;
	}
}
