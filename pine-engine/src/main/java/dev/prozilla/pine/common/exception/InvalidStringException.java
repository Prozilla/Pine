package dev.prozilla.pine.common.exception;

/**
 * Thrown to indicate that a method has been passed an invalid string.
 */
public class InvalidStringException extends IllegalArgumentException {
	
	/**
	 * Creates an invalid string exception with no detail message.
	 */
	public InvalidStringException() {
		super();
	}
	
	/**
	 * Creates an invalid string exception with a detail message.
	 */
	public InvalidStringException(String message) {
		super(message);
	}
	
}
