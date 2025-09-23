package dev.prozilla.pine.common.opengl;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;

/**
 * OpenGL utilities.
 */
public final class GLUtils {
	
	private GLUtils() {}
	
	/**
	 * Translates an OpenGL error code to a string describing the error.
	 * Source: <a href="https://github.com/LWJGL/lwjgl3/commit/ae20e0c9c4d8fda9b09160500955e0976d1cc208#diff-ab7fadcd0e9d099b2dc851ea69e7e2c6dacb2ff53a6c57d91269d19e97e1cc35L45">GLUtil.java</a>.
	 * @param errorCode The error code, as returned by {@link GL11#glGetError}
	 * @return The error description.
	 */
	public static @NotNull String getErrorString(int errorCode) {
		return switch (errorCode) {
			case GL_NO_ERROR -> "No error";
			case GL_INVALID_ENUM -> "Enum argument out of range";
			case GL_INVALID_VALUE -> "Numeric argument out of range";
			case GL_INVALID_OPERATION -> "Operation illegal in current state";
			case GL_STACK_OVERFLOW -> "Command would cause a stack overflow";
			case GL_STACK_UNDERFLOW -> "Command would cause a stack underflow";
			case GL_OUT_OF_MEMORY -> "Not enough memory left to execute command";
			case GL_INVALID_FRAMEBUFFER_OPERATION -> "Framebuffer object is not complete";
			case GL_TABLE_TOO_LARGE -> "The specified table is too large";
			default -> "Unknown error";
		};
	}
	
}
