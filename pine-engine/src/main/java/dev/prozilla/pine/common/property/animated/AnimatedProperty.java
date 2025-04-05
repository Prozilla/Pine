package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.property.VariableProperty;

/**
 * A property with a value that changes over time.
 */
public abstract class AnimatedProperty<T> extends VariableProperty<T> {

	protected final T start, end;
	protected final AnimationCurve curve;

	protected float time;
	
	/**
	 * Creates a property with an animation.
	 * @param start Value at the start of the animation
	 * @param end Value at the end of the animation
	 */
	public AnimatedProperty(T start, T end, AnimationCurve curve) {
		this.start = start;
		this.end = end;
		this.curve = curve;
		
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
	
	/**
	 * Progresses the animation.
	 * @param deltaTime How far to progress the animation, in seconds
	 */
	public void update(float deltaTime) {
		time += deltaTime;
	}
	
	protected float getFactor() {
		return curve.evaluate(time);
	}
	
	public boolean hasFinished() {
		return time >= curve.duration;
	}
	
	public void setDuration(float duration) {
		curve.duration = duration;
	}
}
