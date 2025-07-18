package dev.prozilla.pine.core.state.input.gamepad;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for gamepad axes.
 * See: <a href="https://www.glfw.org/docs/3.3/group__gamepad__axes.html">GLFW: Gamepad axes</a>
 */
public enum GamepadAxis implements IntEnum {
	
	LEFT_X(GLFW_GAMEPAD_AXIS_LEFT_X),
	LEFT_Y(GLFW_GAMEPAD_AXIS_LEFT_Y),
	RIGHT_X(GLFW_GAMEPAD_AXIS_RIGHT_X),
	RIGHT_Y(GLFW_GAMEPAD_AXIS_RIGHT_Y),
	LEFT_TRIGGER(GLFW_GAMEPAD_AXIS_LEFT_TRIGGER),
	RIGHT_TRIGGER(GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);
	
	private final int value;
	
	GamepadAxis(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if a given value is a valid value for a GLFW gamepad axis.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		return EnumUtils.hasIntValue(values(), value);
	}
}
