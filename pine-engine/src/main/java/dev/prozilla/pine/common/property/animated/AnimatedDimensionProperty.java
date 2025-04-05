package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;

public class AnimatedDimensionProperty extends AnimatedProperty<DimensionBase> {
	
	protected final Dimension.Mix mixedDimension;
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, AnimationCurve curve) {
		super(start, end, curve);
		mixedDimension = new Dimension.Mix(start, end);
	}
	
	@Override
	public DimensionBase getValue() {
		mixedDimension.setFactor(getFactor());
		return mixedDimension;
	}
}
