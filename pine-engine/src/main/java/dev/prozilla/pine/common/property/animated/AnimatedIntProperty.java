package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.IntProperty;

public class AnimatedIntProperty extends AnimatedProperty<Integer> implements IntProperty {
	
	protected int start;
	protected int end;
	
	/**
	 * Creates an integer property with an animation.
	 * @param start The value at the start of the animation
	 * @param end The value at the end of the animation
	 * @param curve The animation curve that determines how this animation progresses over time
	 */
	public AnimatedIntProperty(int start, int end, AnimationCurve curve) {
		super(curve);
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Restarts the animation and returns the current value.
	 */
	public int getRestarted() {
		restart();
		return get();
	}
	
	/**
	 * Updates the animation and returns the current value.
	 * @param deltaTime Delta time in seconds.
	 */
	public int getUpdated(float deltaTime) {
		update(deltaTime);
		return get();
	}
	
	@Override
	public int get() {
		return Math.round(MathUtils.remap(getProgress(), 0f, 1f, start, end));
	}
	
}
