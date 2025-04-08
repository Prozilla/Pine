package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.util.Numbers;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Determines how an animation progresses over time.
 */
public class AnimationCurve {
	
	public float duration;
	public final EasingFunction easingFunction;
	public final AnimationDirection direction;
	
	public static final EasingFunction DEFAULT_EASING_FUNCTION = Easing.LINEAR;
	public static final AnimationDirection DEFAULT_DIRECTION = AnimationDirection.NORMAL;
	
	/**
	 * Creates a linear animation with a given duration.
	 * @param duration Duration of the animation
	 */
	public AnimationCurve(float duration) {
		this(duration, DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Creates an animation with a given duration.
	 * @param duration Duration of the animation
	 */
	public AnimationCurve(float duration, EasingFunction easingFunction) {
		this(duration, easingFunction, DEFAULT_DIRECTION);
	}
	
	/**
	 * Creates an animation with a given duration.
	 * @param duration Duration of the animation
	 */
	public AnimationCurve(float duration, EasingFunction easingFunction, AnimationDirection direction) {
		this.duration = Numbers.requirePositive(duration, true, "duration must be strictly positive");
		this.easingFunction = Objects.requireNonNull(easingFunction);
		this.direction = Objects.requireNonNull(direction);
	}
	
	/**
	 * Evaluates this curve at a given time.
	 */
	public float evaluate(float time) {
		return easingFunction.get(direction.get(time, duration));
	}
	
	public boolean equals(AnimationCurve animationCurve) {
		return animationCurve.duration == duration && animationCurve.easingFunction == easingFunction && animationCurve.direction == direction;
	}
	
	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner(" ");
		
		stringJoiner.add(duration + "s");
		
		if (easingFunction != DEFAULT_EASING_FUNCTION || direction != DEFAULT_DIRECTION) {
			stringJoiner.add(easingFunction.toString());
			
			if (direction != DEFAULT_DIRECTION) {
				stringJoiner.add(direction.toString());
			}
		}
		
		return stringJoiner.toString();
	}
}
