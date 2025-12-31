package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;

public class AnimatedDimensionProperty extends AnimatedObjectProperty<DimensionBase> {
	
	protected final Dimension.Mix mixedDimension;
	
	/**
	 * Creates a dimension property with an animation.
	 * @param start The dimension at the start of the animation
	 * @param end The dimension at the end of the animation
	 * @param curve The animation curve that determines how this animation progresses over time
	 */
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, AnimationCurve curve) {
		super(start, end, curve);
		mixedDimension = new Dimension.Mix(start, end);
	}
	
	@Override
	public DimensionBase getValue() {
		mixedDimension.setFactor(getProgress());
		return mixedDimension;
	}
}
