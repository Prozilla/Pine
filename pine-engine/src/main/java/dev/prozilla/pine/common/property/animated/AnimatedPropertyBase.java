package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.lifecycle.Updatable;
import dev.prozilla.pine.common.property.Property;

public interface AnimatedPropertyBase<T> extends Property<T>, Updatable {
	
	/**
	 * Restarts the animation.
	 */
	void restart();
	
	/**
	 * Restarts the animation and returns the current value.
	 */
	default T getRestartedValue() {
		restart();
		return getValue();
	}
	
	/**
	 * Updates the animation and returns the current value.
	 * @param deltaTime Delta time in seconds.
	 */
	default T getUpdatedValue(float deltaTime) {
		update(deltaTime);
		return getValue();
	}
	
	/**
	 * Progresses the animation.
	 * @param deltaTime How far to progress the animation, in seconds
	 */
	@Override
	void update(float deltaTime);
	
	/**
	 * @deprecated Replaced by {@link #getProgress()} as of 3.0.2
	 */
	@Deprecated
	default float getFactor() {
		return getProgress();
	}
	
	/**
	 * Checks if the animation has finished.
	 * @return {@code true} if the animation has finished.
	 */
	default boolean hasFinished() {
		return getProgress() >= 1;
	}
	
	/**
	 * Returns the progress of the animation, as a value between {@code 0f} and {@code 1f}.
	 * @return The progress of the animation.
	 */
	float getProgress();
	
	/**
	 * Sets the duration of the animation.
	 * @param duration The new duration of the animation, in seconds
	 */
	void setDuration(float duration);
	
	/**
	 * Returns the animation curve of this property, which determines how the animation progresses over time.
	 * @return The animation curve of this property.
	 */
	AnimationCurve getCurve();
	
}
