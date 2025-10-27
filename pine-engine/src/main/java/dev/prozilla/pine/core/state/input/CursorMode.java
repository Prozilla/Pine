package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import java.security.InvalidParameterException;

import static org.lwjgl.glfw.GLFW.*;

public enum CursorMode implements IntEnum {
	
	/** Makes the cursor visible and makes the cursor behave normally. */
	NORMAL(GLFW_CURSOR_NORMAL),
	/** Makes the cursor invisible when it is hovering over the content area of the window. */
	HIDDEN(GLFW_CURSOR_HIDDEN),
	/** Makes the cursor invisible and locks its position, but still provides virtual cursor movement. */
	DISABLED(GLFW_CURSOR_DISABLED);

	private final int value;
	
	CursorMode(int value) {
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
	 * @return Cursor mode
	 */
	public static CursorMode parse(int value) {
		if (!isValid(value)) {
			throw new InvalidParameterException("Invalid cursor type: " + value);
		}
		
		return values()[value];
	}
	
}
