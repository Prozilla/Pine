package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for mouse buttons.
 * See: <a href="https://www.glfw.org/docs/3.3/group__buttons.html">GLFW: Mouse buttons</a>
 */
public enum MouseButton implements IntEnum {
	
	LEFT(GLFW_MOUSE_BUTTON_1),
	RIGHT(GLFW_MOUSE_BUTTON_2),
	MIDDLE(GLFW_MOUSE_BUTTON_3),
	EXTRA_0(GLFW_MOUSE_BUTTON_4),
	EXTRA_1(GLFW_MOUSE_BUTTON_5),
	EXTRA_2(GLFW_MOUSE_BUTTON_6),
	EXTRA_3(GLFW_MOUSE_BUTTON_7),
	EXTRA_4(GLFW_MOUSE_BUTTON_8);
	
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
		return EnumUtils.hasIntValue(values(), value);
	}
}
