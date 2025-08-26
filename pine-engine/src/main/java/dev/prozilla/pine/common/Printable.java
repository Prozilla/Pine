package dev.prozilla.pine.common;

import dev.prozilla.pine.common.logging.Logger;

import java.util.Collection;
import java.util.StringJoiner;

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
	
	static <P extends Printable> void printAll(Collection<P> printables) {
		printAll(printables, Logger.system);
	}
	
	static <P extends Printable> void printAll(Collection<P> printables, Logger logger) {
		if (printables.isEmpty()) {
			logger.log("[]");
		} else if (printables.size() == 1) {
			logger.logf("[%s]", printables.iterator().next());
		} else {
			StringJoiner stringJoiner = new StringJoiner(",\n");
			for (P printable : printables) {
				stringJoiner.add("\t" + printable.toString());
			}
			logger.logf("[%n%s%n]", stringJoiner);
		}
	}
	
}
