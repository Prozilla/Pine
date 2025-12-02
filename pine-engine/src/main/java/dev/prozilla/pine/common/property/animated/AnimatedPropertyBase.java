package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.lifecycle.Updatable;
import dev.prozilla.pine.common.property.Property;

public interface AnimatedPropertyBase<T> extends Property<T>, Updatable {
	
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
	
	float getFactor();
	
	boolean hasFinished();
	
	void setDuration(float duration);
	
	AnimationCurve getCurve();
	
}
