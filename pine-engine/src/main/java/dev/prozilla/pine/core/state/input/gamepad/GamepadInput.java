package dev.prozilla.pine.core.state.input.gamepad;

/**
 * Provides input handling for gamepad devices.
 */
public interface GamepadInput {
	
	/**
	 * Gets the current value of an axis.
	 * @return Value of the axis
	 */
	default float getAxis(GamepadAxis axis) {
		return getAxis(axis.getValue());
	}
	
	/**
	 * Gets the current value of an axis.
	 * @param axis GLFW integer value for a gamepad axis
	 * @return Value of the axis
	 */
	float getAxis(int axis);
	
	/**
	 * Checks whether a button is being pressed.
	 * @return {@code true} if the button is being pressed
	 */
	default boolean getButton(GamepadButton button) {
		return getButton(button.getValue());
	}
	
	/**
	 * Checks whether a button is being pressed.
	 * @param button GLFW integer value for a gamepad button
	 * @return {@code true} if the button is being pressed
	 */
	boolean getButton(int button);
	
	default boolean getButtonDown(GamepadButton button) {
		return getButtonDown(button.getValue());
	}
	
	boolean getButtonDown(int button);
	
}
