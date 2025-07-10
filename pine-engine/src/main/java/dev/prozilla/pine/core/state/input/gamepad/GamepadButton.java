package dev.prozilla.pine.core.state.input.gamepad;

import dev.prozilla.pine.common.util.ArrayUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for gamepad buttons.
 * See: <a href="https://www.glfw.org/docs/3.3/group__gamepad__buttons.html">GLFW: Gamepad buttons</a>
 */
public enum GamepadButton {
	
	A(GLFW_GAMEPAD_BUTTON_A),
	B(GLFW_GAMEPAD_BUTTON_B),
	X(GLFW_GAMEPAD_BUTTON_X),
	Y(GLFW_GAMEPAD_BUTTON_Y),
	CROSS(GLFW_GAMEPAD_BUTTON_A),
	CIRCLE(GLFW_GAMEPAD_BUTTON_B),
	SQUARE(GLFW_GAMEPAD_BUTTON_X),
	TRIANGLE(GLFW_GAMEPAD_BUTTON_Y),
	LEFT_BUMPER(GLFW_GAMEPAD_BUTTON_LEFT_BUMPER),
	RIGHT_BUMPER(GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER),
	BACK(GLFW_GAMEPAD_BUTTON_BACK),
	START(GLFW_GAMEPAD_BUTTON_START),
	GUIDE(GLFW_GAMEPAD_BUTTON_GUIDE),
	LEFT_THUMB(GLFW_GAMEPAD_BUTTON_LEFT_THUMB),
	RIGHT_THUMB(GLFW_GAMEPAD_BUTTON_RIGHT_THUMB),
	DPAD_UP(GLFW_GAMEPAD_BUTTON_DPAD_UP),
	DPAD_RIGHT(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT),
	DPAD_DOWN(GLFW_GAMEPAD_BUTTON_DPAD_DOWN),
	DPAD_LEFT(GLFW_GAMEPAD_BUTTON_DPAD_LEFT);
	
	private final int value;
	
	GamepadButton(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if a given value is a valid value for a GLFW gamepad button.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		return ArrayUtils.contains(values(), value);
	}
}
