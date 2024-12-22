package dev.prozilla.pine.common;

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
		System.out.println(this);
	}
}
