package dev.prozilla.pine.common;

/**
 * Abstract interface for objects that can be animated.
 */
public interface Animatable {
	
	/**
	 * Restarts the animation from the beginning.
	 */
	void restartAnimation();
	
	/**
	 * Progresses the animation in time.
	 * @param deltaTime Time between this frame and the last one, in seconds.
	 */
	void updateAnimation(float deltaTime);
	
}
