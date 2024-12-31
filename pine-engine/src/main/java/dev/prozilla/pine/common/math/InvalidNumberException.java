package dev.prozilla.pine.common.math;

/**
 * Thrown to indicate that a method has been passed an invalid number value.
 */
public class InvalidNumberException extends RuntimeException {
	
	/**
	 * Creates an invalid number exception with no detail message.
	 */
	public InvalidNumberException() {
		super();
	}
	
	/**
	 * Creates an invalid number exception with a detail message.
	 */
	public InvalidNumberException(String message) {
		super(message);
	}
	
}
