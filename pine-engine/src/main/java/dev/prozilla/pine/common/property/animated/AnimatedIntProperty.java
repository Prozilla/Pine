package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.IntProperty;

public class AnimatedIntProperty extends AnimatedProperty<Integer> implements IntProperty {
	
	protected int start;
	protected int end;
	
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
		return Math.round(MathUtils.remap(getFactor(), 0f, 1f, start, end));
	}
	
}
