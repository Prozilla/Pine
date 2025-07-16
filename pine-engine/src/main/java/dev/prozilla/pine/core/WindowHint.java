package dev.prozilla.pine.core;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @see <a href="https://www.glfw.org/docs/3.3/window_guide.html">GLFW: Window guide</a>
 */
public enum WindowHint implements IntEnum {
	
	DECORATED(GLFW_DECORATED),
	VISIBLE(GLFW_VISIBLE),
	ENABLE_TRANSPARENT_FRAMEBUFFER(GLFW_TRANSPARENT_FRAMEBUFFER),
	RESIZABLE(GLFW_RESIZABLE),
	FOCUSED(GLFW_FOCUSED),
	AUTO_ICONIFY(GLFW_AUTO_ICONIFY),
	FLOATING(GLFW_FLOATING),
	MAXIMIZED(GLFW_MAXIMIZED),
	CENTER_CURSOR(GLFW_CENTER_CURSOR),
	FOCUS_ON_SHOW(GLFW_FOCUS_ON_SHOW),
	SCALE_TO_MONITOR(GLFW_SCALE_TO_MONITOR);
	
	private final int value;
	
	WindowHint(int value) {
		this.value = value;
	}
	
	@Override
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if a given value is a valid value for a GLFW window hint.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		return EnumUtils.hasIntValue(values(), value);
	}

}
