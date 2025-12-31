package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.core.state.input.Input;

public interface InputProperty {
	
	/**
	 * Returns a boolean property whose value is {@code true} if the input key is pressed.
	 * @return A boolean property based on whether the input key is pressed.
	 * @see #isPressed(Input)
	 */
	default BooleanProperty isPressedProperty(Input input) {
		return () -> isPressed(input);
	}
	
	/**
	 * Checks if the input key is pressed.
	 * @return {@code true} if the input key is pressed.
	 */
	boolean isPressed(Input input);
	
	/**
	 * Returns a boolean property whose value is {@code true} if the input key is down.
	 * @return A boolean property based on whether the input key is down.
	 * @see #isDown(Input)
	 */
	default BooleanProperty isDownProperty(Input input) {
		return () -> isDown(input);
	}
	
	/**
	 * Checks if the input key is down.
	 * Returns {@code true} in the first frame that the key is pressed.
	 * @return {@code true} if the input key is pressed.
	 */
	boolean isDown(Input input);
	
}
