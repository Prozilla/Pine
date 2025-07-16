package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import java.security.InvalidParameterException;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for cursor types.
 * See: <a href="https://www.glfw.org/docs/3.3/group__shapes.html">GLFW: Standard cursor shapes</a>
 */
public enum CursorType implements IntEnum {

	DEFAULT(GLFW_ARROW_CURSOR),
	HAND(GLFW_HAND_CURSOR),
	TEXT(GLFW_IBEAM_CURSOR),
	CROSSHAIR(GLFW_CROSSHAIR_CURSOR),
	RESIZE_HORIZONTAL(GLFW_HRESIZE_CURSOR),
	RESIZE_VERTICAL(GLFW_VRESIZE_CURSOR);

	private final int value;
	
	CursorType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if a given value is a valid value for a cursor type.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		return EnumUtils.hasIntValue(values(), value);
	}
	
	/**
	 * Parses a given value to a cursor type
	 * @param value Value
	 * @return Cursor type
	 */
	public static CursorType parse(int value) {
		if (!isValid(value)) {
			throw new InvalidParameterException("Invalid cursor type: " + value);
		}
		
		return values()[value];
	}
}
