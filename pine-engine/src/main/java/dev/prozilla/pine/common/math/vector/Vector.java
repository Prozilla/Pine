package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.Printable;

/**
 * Abstract interface for vectors.
 */
public interface Vector<V extends Vector<V>> extends Printable {
	
	/**
	 * Calculates the length of this vector.
	 */
	float length();
	
	/**
	 * Normalizes this vector.
	 * @return Self.
	 */
	default V normalize() {
		return divide(length());
	}
	
	/**
	 * Adds another vector to this vector.
	 * @return Self
	 */
	V add(V vector);
	
	/**
	 * Negates this vector.
	 * @return Self
	 */
	default V negate() {
		return scale(-1f);
	}
	
	/**
	 * Subtracts another vector from this vector.
	 * @return Self
	 */
	default V subtract(V vector) {
		return add(vector.negate());
	}
	
	/**
	 * Scales this vector by a scalar.
	 * @return Self
	 */
	V scale(float scalar);
	
	/**
	 * Divides this vector by a scalar.
	 * @return Self
	 */
	default V divide(float scalar) {
		return scale(1f / scalar);
	}
	
	/**
	 * Calculates a linear interpolation between this vector with another vector.
	 * @param alpha The alpha value, in the range of <code>0f</code> and <code>1f</code>
	 * @return Self
	 */
	default V lerp(V vector, float alpha) {
		return scale(1f - alpha).add(vector.scale(alpha));
	}
	
	/**
	 * Converts this vector to a string representation.
	 */
	@Override
	String toString();
	
	/**
	 * Prints the string representation of this vector.
	 */
	@Override
	default void print() {
		System.out.println(this);
	}
}
