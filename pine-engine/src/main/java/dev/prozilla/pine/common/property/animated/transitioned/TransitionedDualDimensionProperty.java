package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.animated.AnimationCurve;

public class TransitionedDualDimensionProperty extends TransitionedProperty<DualDimension> {
	
	protected Dimension.Mix mixedDimensionX;
	protected Dimension.Mix mixedDimensionY;
	protected DualDimension result;
	
	public TransitionedDualDimensionProperty(DualDimension initialValue, AnimationCurve curve) {
		super(initialValue, curve);
	}
	
	@Override
	public void transitionTo(DualDimension targetValue) {
		super.transitionTo(targetValue);
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
