package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.easing.EasingFunction;

public class AnimatedDualDimensionProperty extends AnimatedProperty<DualDimension> {
	
	protected final Dimension.Mix mixedDimensionX;
	protected final Dimension.Mix mixedDimensionY;
	protected final DualDimension result;
	
	public AnimatedDualDimensionProperty(DualDimension start, DualDimension end, float duration) {
		this(start, end, duration, DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedDualDimensionProperty(DualDimension start, DualDimension end, float duration, EasingFunction easingFunction) {
		this(start, end, duration, easingFunction, DEFAULT_DIRECTION);
	}
	
	public AnimatedDualDimensionProperty(DualDimension start, DualDimension end, float duration, EasingFunction easingFunction, AnimationDirection direction) {
		super(start, end, duration, easingFunction, direction);
		mixedDimensionX = new Dimension.Mix(start.x, end.x);
		mixedDimensionY = new Dimension.Mix(start.y, end.y);
		result = new DualDimension(mixedDimensionX, mixedDimensionY);
	}
	
	@Override
	public DualDimension getValue() {
		mixedDimensionX.setFactor(getFactor());
		mixedDimensionY.setFactor(getFactor());
		return result;
	}
}
