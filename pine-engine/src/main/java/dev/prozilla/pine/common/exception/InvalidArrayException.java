package dev.prozilla.pine.common.exception;

/**
 * Thrown to indicate that a method has been passed an invalid array.
 */
public class InvalidArrayException extends IllegalArgumentException {
	
	/**
	 * Creates an invalid array exception with no detail message.
	 */
	public InvalidArrayException() {
		super();
	}
	
	/**
	 * Creates an invalid array exception with a detail message.
	 */
	public InvalidArrayException(String message) {
		super(message);
	}
	
}
