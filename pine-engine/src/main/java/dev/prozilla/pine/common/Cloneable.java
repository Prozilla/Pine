package dev.prozilla.pine.common;

/**
 * Abstract interface for cloneable objects.
 * A cloneable object should have a <code>clone()</code> method that returns a new instance
 * and an <code>equals(object)</code> method that returns <code>true</code> when used with the clone.
 * @param <O> Type of the cloneable object
 */
public interface Cloneable<O extends Cloneable<O>> {
	
	/**
	 * Checks if the given object is equal to this object.
	 * @param other Other object
	 * @return <code>true</code> if both objects are equal.
	 */
	boolean equals(O other);
	
	/**
	 * Returns a new object that is equal to this object.
	 * @return Clone of this object
	 */
	O clone();
	
}
