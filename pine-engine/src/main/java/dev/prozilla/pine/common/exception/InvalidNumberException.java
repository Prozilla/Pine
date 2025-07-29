package dev.prozilla.pine.common.exception;

/**
 * Thrown to indicate that a method has been passed an invalid number value.
 */
public class InvalidNumberException extends PineException {
	
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
