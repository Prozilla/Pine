package dev.prozilla.pine.common;

import dev.prozilla.pine.common.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract interface for printable objects.
 */
public interface Printable {
	
	/**
	 * Returns a string representation of this object.
	 * @return String representation of this object.
	 */
	@NotNull String toString();
	
	/**
	 * Prints this object using the system logger then terminates the line.
	 */
	default void print() {
		print(Logger.system);
	}
	
	/**
	 * Prints this object then terminates the line.
	 */
	default void print(Logger logger) {
		logger.log(this);
	}
	
}
