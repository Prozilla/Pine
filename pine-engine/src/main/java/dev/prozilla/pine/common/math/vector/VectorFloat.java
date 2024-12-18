package dev.prozilla.pine.common.math.vector;

import java.nio.FloatBuffer;

/**
 * Abstract interface for vectors with floating point precision.
 */
public interface VectorFloat<V extends VectorFloat<V>> extends Vector<V> {
	
	@Override
	default float length() {
		return (float)Math.sqrt(lengthSquared());
	}
	
	/**
	 * Calculates the squared length of this vector.
	 */
	float lengthSquared();
	
	/**
	 * Calculates the dot product of this vector with another vector.
	 * @return Dot product of this vector multiplied by another vector
	 */
	float dot(V vector);
	
	/**
	 * Stores the vector in a given buffer.
	 * @param buffer The buffer to store the vector data in
	 */
	void toBuffer(FloatBuffer buffer);
	
}
