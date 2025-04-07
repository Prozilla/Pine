package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;

public class TransitionedDimensionProperty extends TransitionedProperty<DimensionBase> {
	
	protected Dimension.Mix result;
	
	public TransitionedDimensionProperty(DimensionBase initialValue, AnimationCurve curve) {
		super(initialValue, curve);
	}
	
	@Override
	public void transitionTo(DimensionBase targetValue) {
		super.transitionTo(targetValue);
		result = new Dimension.Mix(start, end, getFactor());
	}
	
	@Override
	public DimensionBase getValue() {
		float factor = getFactor();
		
		if (factor == 0) {
			return start;
		} else if (factor == 1) {
			return end;
		}
		
		result.setFactor(factor);
		return result;
	}
}
