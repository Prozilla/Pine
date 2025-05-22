package dev.prozilla.pine.core.state.input;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for gamepads.
 * See: <a href="https://www.glfw.org/docs/3.3/group__joysticks.html">GLFW: Joysticks</a>
 */
public enum Gamepad {
	
	ID_0(GLFW_JOYSTICK_1),
	ID_1(GLFW_JOYSTICK_2),
	ID_2(GLFW_JOYSTICK_3),
	ID_3(GLFW_JOYSTICK_4),
	ID_4(GLFW_JOYSTICK_5),
	ID_5(GLFW_JOYSTICK_6),
	ID_6(GLFW_JOYSTICK_7),
	ID_7(GLFW_JOYSTICK_8),
	ID_8(GLFW_JOYSTICK_9),
	ID_9(GLFW_JOYSTICK_10);
	
	private final int value;
	
	Gamepad(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if a given value is a valid value for a GLFW gamepad.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		boolean valid = false;
		for (Gamepad key : values()) {
			if (key.value == value) {
				valid = true;
				break;
			}
		}
		return valid;
	}
}
