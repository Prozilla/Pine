package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.FloatProperty;

public class AnimatedFloatProperty extends AnimatedProperty<Float> implements FloatProperty {
	
	protected float start;
	protected float end;
	
	public AnimatedFloatProperty(float start, float end, AnimationCurve curve) {
		super(curve);
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Restarts the animation and returns the current value.
	 */
	public float getRestarted() {
		restart();
		return get();
	}
	
	/**
	 * Updates the animation and returns the current value.
	 * @param deltaTime Delta time in seconds.
	 */
	public float getUpdated(float deltaTime) {
		update(deltaTime);
		return get();
	}
	
	@Override
	public float get() {
		return MathUtils.remap(getFactor(), 0f, 1f, start, end);
	}
	
}
