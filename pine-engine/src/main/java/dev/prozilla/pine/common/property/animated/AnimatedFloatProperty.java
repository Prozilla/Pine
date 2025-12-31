package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.FloatProperty;

public class AnimatedFloatProperty extends AnimatedProperty<Float> implements FloatProperty {
	
	protected float start;
	protected float end;
	
	/**
	 * Creates a float property with an animation.
	 * @param start The value at the start of the animation
	 * @param end The value at the end of the animation
	 * @param curve The animation curve that determines how this animation progresses over time
	 */
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
		return MathUtils.remap(getProgress(), 0f, 1f, start, end);
	}
	
}
