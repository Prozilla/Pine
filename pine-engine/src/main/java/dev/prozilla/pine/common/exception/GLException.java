package dev.prozilla.pine.common.exception;

import dev.prozilla.pine.common.lwjgl.GLUtils;

/**
 * An OpenGL error.
 */
public class GLException extends PineException {
	
	public GLException() {}
	
	/**
	 * @param error An OpenGL error code
	 */
	public GLException(int error) {
		this(String.format("%s - %s", error, GLUtils.getErrorString(error)));
	}
	
	public GLException(String message) {
		super(message);
	}
	
}
