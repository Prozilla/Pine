package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;

public class AnimatedDualDimensionProperty extends AnimatedObjectProperty<DualDimension> {
	
	protected final Dimension.Mix mixedDimensionX;
	protected final Dimension.Mix mixedDimensionY;
	protected final DualDimension result;
	
	/**
	 * Creates a dual dimension property with an animation.
	 * @param start The dual dimension at the start of the animation
	 * @param end The dual dimension at the end of the animation
	 * @param curve The animation curve that determines how this animation progresses over time
	 */
	public AnimatedDualDimensionProperty(DualDimension start, DualDimension end, AnimationCurve curve) {
		super(start, end, curve);
		mixedDimensionX = new Dimension.Mix(start.x, end.x);
		mixedDimensionY = new Dimension.Mix(start.y, end.y);
		result = new DualDimension(mixedDimensionX, mixedDimensionY);
	}
	
	@Override
	public DualDimension getValue() {
		mixedDimensionX.setFactor(getProgress());
		mixedDimensionY.setFactor(getProgress());
		return result;
	}
}
