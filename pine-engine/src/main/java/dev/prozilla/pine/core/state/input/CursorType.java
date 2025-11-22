package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import java.security.InvalidParameterException;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for standard cursor shapes.
 * See: <a href="https://www.glfw.org/docs/3.3/group__shapes.html">GLFW: Standard cursor shapes</a>
 */
public enum CursorType implements IntEnum {
	
	/** Regular arrow cursor. */
	DEFAULT(GLFW_ARROW_CURSOR),
	/**
	 * @deprecated Replaced by {@link #POINTER} as of 2.1.0
	 */
	@Deprecated
	HAND(GLFW_POINTING_HAND_CURSOR),
	/** Pointing hand cursor. */
	POINTER(GLFW_POINTING_HAND_CURSOR),
	/** Vertical I-beam cursor used for text input. */
	TEXT(GLFW_IBEAM_CURSOR),
	/** Crosshair cursor shape. */
	CROSSHAIR(GLFW_CROSSHAIR_CURSOR),
	/** Horizontal resize/move arrow. */
	RESIZE_HORIZONTAL(GLFW_HRESIZE_CURSOR),
	/** Vertical resize/move arrow. */
	RESIZE_VERTICAL(GLFW_VRESIZE_CURSOR),
	/** Top-left to bottom-right diagonal resize/move arrow. */
	RESIZE_TOP_LEFT_BOTTOM_RIGHT(GLFW_RESIZE_NWSE_CURSOR),
	/** Top-right to bottom-left diagonal resize/move arrow. */
	RESIZE_TOP_RIGHT_BOTTOM_LEFT(GLFW_RESIZE_NESW_CURSOR),
	/** Omnidirectional resize/move arrow. */
	RESIZE_ALL(GLFW_RESIZE_ALL_CURSOR),
	/** Operation-not-allowed cursor. */
	NOT_ALLOWED(GLFW_NOT_ALLOWED_CURSOR);

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
