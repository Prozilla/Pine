package dev.prozilla.pine.common.property.animated;

/**
 * A property with a value that changes over time.
 */
public abstract class AnimatedObjectProperty<T> extends AnimatedProperty<T> {

	protected T start;
	protected T end;
	
	/**
	 * Creates a property with an animation.
	 * @param start The value at the start of the animation
	 * @param end The value at the end of the animation
	 * @param curve The animation curve that determines how this animation progresses over time
	 */
	public AnimatedObjectProperty(T start, T end, AnimationCurve curve) {
		super(curve);
		this.start = start;
		this.end = end;
	}
	
}
