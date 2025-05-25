package dev.prozilla.pine.common.lifecycle;

public interface InputHandler {
	
	/**
	 * Handles input every frame.
	 * @param deltaTime Delta time in seconds
	 */
	void input(float deltaTime);
	
}
