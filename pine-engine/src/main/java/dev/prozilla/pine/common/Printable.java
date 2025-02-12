package dev.prozilla.pine.common;

import dev.prozilla.pine.common.logging.Logger;

/**
 * Abstract interface for printable objects.
 */
public interface Printable {
	
	/**
	 * Returns a string representation of this object.
	 * @return String representation of this object.
	 */
	String toString();
	
	/**
	 * Prints this object then terminates the line.
	 */
	default void print() {
		print(Logger.system);
	}
	
	default void print(Logger logger) {
		logger.log(this);
	}
}
