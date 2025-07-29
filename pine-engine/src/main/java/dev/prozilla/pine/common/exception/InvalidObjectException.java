package dev.prozilla.pine.common.exception;

/**
 * Thrown to indicate that a method has been passed an invalid object.
 */
public class InvalidObjectException extends PineException {
	
	/**
	 * Creates an invalid object exception with no detail message.
	 */
	public InvalidObjectException() {
		super();
	}
	
	/**
	 * Creates an invalid object exception with a detail message.
	 */
	public InvalidObjectException(String message) {
		super(message);
	}
	
}
