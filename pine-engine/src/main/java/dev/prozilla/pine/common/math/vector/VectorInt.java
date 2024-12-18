package dev.prozilla.pine.common.math.vector;

import java.nio.IntBuffer;

/**
 * Abstract interface for vectors with integer precision.
 */
public interface VectorInt<V extends VectorInt<V>> extends Vector<V> {
	
	@Override
	default float length() {
		return (float)Math.sqrt(lengthSquared());
	}
	
	/**
	 * Calculates the squared length of this vector.
	 */
	int lengthSquared();
	
	/**
	 * Calculates the dot product of this vector with another vector.
	 * @return Dot product of this vector multiplied by another vector
	 */
	int dot(V vector);
	
	/**
	 * Stores the vector in a given buffer.
	 * @param buffer The buffer to store the vector data in
	 */
	void toBuffer(IntBuffer buffer);
	
}
