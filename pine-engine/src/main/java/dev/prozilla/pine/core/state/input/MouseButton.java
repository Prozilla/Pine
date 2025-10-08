package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for mouse buttons.
 * See: <a href="https://www.glfw.org/docs/3.3/group__buttons.html">GLFW: Mouse buttons</a>
 */
public enum MouseButton implements IntEnum {
	
	LEFT(GLFW_MOUSE_BUTTON_1, "Left Mouse Button"),
	RIGHT(GLFW_MOUSE_BUTTON_2, "Right Mouse Button"),
	MIDDLE(GLFW_MOUSE_BUTTON_3, "Middle Mouse Button"),
	EXTRA_0(GLFW_MOUSE_BUTTON_4, "X1 Mouse Button"),
	EXTRA_1(GLFW_MOUSE_BUTTON_5, "X2 Mouse Button"),
	EXTRA_2(GLFW_MOUSE_BUTTON_6, "X3 Mouse Button"),
	EXTRA_3(GLFW_MOUSE_BUTTON_7, "X4 Mouse Button"),
	EXTRA_4(GLFW_MOUSE_BUTTON_8, "X5 Mouse Button");
	
	private final int value;
	private final String string;
	
	MouseButton(int value, String string) {
		this.value = value;
		this.string = string;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return string;
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
