package dev.prozilla.pine.common.property.animated;

/**
 * A property with a value that changes over time.
 */
public abstract class AnimatedObjectProperty<T> extends AnimatedProperty<T> {

	protected T start;
	protected T end;
	
	/**
	 * Creates a property with an animation.
	 * @param start Value at the start of the animation
	 * @param end Value at the end of the animation
	 */
	public AnimatedObjectProperty(T start, T end, AnimationCurve curve) {
		super(curve);
		this.start = start;
		this.end = end;
	}
	
}
