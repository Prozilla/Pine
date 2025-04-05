package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.DimensionProperty;

public class AnimatedDimensionProperty extends AnimatedProperty<DimensionBase> implements DimensionProperty {
	
	protected final Dimension.Mix mixedDimension;
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, float duration) {
		this(start, end, duration, DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, float duration, EasingFunction easingFunction) {
		super(start, end, duration, easingFunction);
		this.mixedDimension = new Dimension.Mix(start, end);
	}
	
	@Override
	public DimensionBase getValue() {
		mixedDimension.setFactor(getFactor());
		return mixedDimension;
	}
}
