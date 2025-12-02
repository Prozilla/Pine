package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;

public class AnimatedDualDimensionProperty extends AnimatedObjectProperty<DualDimension> {
	
	protected final Dimension.Mix mixedDimensionX;
	protected final Dimension.Mix mixedDimensionY;
	protected final DualDimension result;
	
	public AnimatedDualDimensionProperty(DualDimension start, DualDimension end, AnimationCurve curve) {
		super(start, end, curve);
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
