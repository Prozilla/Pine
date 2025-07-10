package dev.prozilla.pine.common.lifecycle;

public interface Updatable {
	
	/**
	 * Updates this object every frame.
	 * @param deltaTime Delta time in seconds
	 */
	void update(float deltaTime);
	
}
