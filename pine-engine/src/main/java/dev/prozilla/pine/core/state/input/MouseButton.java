package dev.prozilla.pine.core.state.input;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseButton {
	
	LEFT(GLFW_MOUSE_BUTTON_1),
	RIGHT(GLFW_MOUSE_BUTTON_2);
	
	private final int value;
	
	MouseButton(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if a given value is a valid value for a GLFW mouse button.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		boolean valid = false;
		for (MouseButton mouseButton : values()) {
			if (mouseButton.value == value) {
				valid = true;
				break;
			}
		}
		return valid;
	}
}
