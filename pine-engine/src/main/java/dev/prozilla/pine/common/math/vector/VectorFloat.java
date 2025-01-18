package dev.prozilla.pine.common.math.vector;

import java.nio.FloatBuffer;

/**
 * Abstract class for vectors with floating point precision.
 */
public abstract class VectorFloat<V extends VectorFloat<V>> extends Vector<V> {
	
	@Override
	public float length() {
		return (float)Math.sqrt(lengthSquared());
	}
	
	/**
	 * Calculates the squared length of this vector.
	 */
	abstract public float lengthSquared();
	
	/**
	 * Calculates the dot product of this vector with another vector.
	 * @return Dot product of this vector multiplied by another vector
	 */
	abstract public float dot(V vector);
	
	/**
	 * Stores the vector in a given buffer.
	 * @param buffer The buffer to store the vector data in
	 */
	abstract public void toBuffer(FloatBuffer buffer);
}
