package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.VariableProperty;

public abstract class AnimatedProperty<T> extends VariableProperty<T> {

	protected T start, end;
	protected EasingFunction easingFunction;
	protected float duration;
	protected float time;
	
	public static final EasingFunction DEFAULT_EASING_FUNCTION = Easing.LINEAR;
	
	public AnimatedProperty(T start, T end, float duration) {
		this(start, end, duration, DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedProperty(T start, T end, float duration, EasingFunction easingFunction) {
		this.start = start;
		this.end = end;
		this.duration = duration;
		this.easingFunction = easingFunction;
		
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
	
	public boolean hasCompleted() {
		return time >= duration;
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}
}
