package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.Easing;
import dev.prozilla.pine.common.property.VariableProperty;

public abstract class AnimatedProperty<T> extends VariableProperty<T> {

	protected T start, end;
	protected Easing easing;
	protected float duration;
	protected float time;
	
	public static final Easing DEFAULT_EASING = Easing.LINEAR;
	
	public AnimatedProperty(T start, T end, float duration) {
		this(start, end, duration, DEFAULT_EASING);
	}
	
	public AnimatedProperty(T start, T end, float duration, Easing easing) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		this.easing = easing;
		
		if (duration == 0) {
			throw new IllegalArgumentException("duration must not be 0");
		}
		
		restart();
	}
	
	/**
	 * Restarts the animation and returns the current value.
	 */
	public T getRestartedValue() {
		restart();
		return getValue();
	}
	
	public void restart() {
		time = 0;
	}
	
	/**
	 * Updates the animation and returns the current value.
	 * @param deltaTime Delta time in seconds.
	 */
	public T getUpdatedValue(float deltaTime) {
		update(deltaTime);
		return getValue();
	}
	
	public void update(float deltaTime) {
		time += deltaTime;
		
		// Clamp time value to duration
		if (time > duration) {
			time = duration;
		}
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}
}
