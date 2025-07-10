package dev.prozilla.pine.common.exception;

/**
 * Thrown to indicate that a method has been passed an invalid collection.
 */
public class InvalidCollectionException extends IllegalArgumentException {
	
	/**
	 * Creates an invalid collection exception with no detail message.
	 */
	public InvalidCollectionException() {
		super();
	}
	
	/**
	 * Creates an invalid collection exception with a detail message.
	 */
	public InvalidCollectionException(String message) {
		super(message);
	}
	
}
