package dev.prozilla.pine.core;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Window hints can be set before the creation of a window and context and can affect the window itself, the framebuffer or the context.
 *
 * <p>Some hints are platform specific. These are always valid to set on any platform, but they will only affect their specific platform. Other platforms will ignore them.</p>
 * @see <a href="https://www.glfw.org/docs/3.3/window_guide.html">GLFW: Window guide</a>
 */
public enum WindowHint implements IntEnum {
	
	/** Specifies whether the windowed mode window will have window decorations such as border, a close widget, etc. */
	DECORATED(GLFW_DECORATED),
	/** Specifies whether the windowed mode window will be initially visible. */
	VISIBLE(GLFW_VISIBLE),
	/** Specifies whether the window framebuffer will be transparent. */
	ENABLE_TRANSPARENT_FRAMEBUFFER(GLFW_TRANSPARENT_FRAMEBUFFER),
	/** Specifies whether the windowed mode window will be resizable by the user. */
	RESIZABLE(GLFW_RESIZABLE),
	/** Specifies whether the windowed mode window will be given input focus when created. */
	FOCUSED(GLFW_FOCUSED),
	/** Specifies whether the fullscreen window will automatically iconify and restore the previous video mode on input focus loss. */
	AUTO_ICONIFY(GLFW_AUTO_ICONIFY),
	/** Specifies whether the windowed mode window will be floating above other regular windows, also called always-on-top. */
	FLOATING(GLFW_FLOATING),
	/** Specifies whether the windowed mode window will be maximized when created. */
	MAXIMIZED(GLFW_MAXIMIZED),
	/** Specifies whether the cursor should be centered over newly created fullscreen windows. */
	CENTER_CURSOR(GLFW_CENTER_CURSOR),
	/** Specifies whether the window will be given input focus when shown. */
	FOCUS_ON_SHOW(GLFW_FOCUS_ON_SHOW),
	/** Specifies whether the window should be resized based on the monitor content scale. */
	SCALE_TO_MONITOR(GLFW_SCALE_TO_MONITOR),
	/** Specifies the major version of the OpenGL context. */
	GL_VERSION_MAJOR(GLFW_CONTEXT_VERSION_MAJOR),
	/** Specifies the minor version of the OpenGL context. */
	GL_VERSION_MINOR(GLFW_CONTEXT_VERSION_MINOR),
	/** Specifies whether the OpenGL context should be forward-compatible. */
	GL_FORWARD_COMPATIBLE(GLFW_OPENGL_FORWARD_COMPAT),
	/** Specifies which OpenGL profile to create the context for. */
	GL_PROFILE(GLFW_OPENGL_PROFILE);
	
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
