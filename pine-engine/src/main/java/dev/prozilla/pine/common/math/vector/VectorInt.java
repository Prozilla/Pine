package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.math.MathUtils;

import java.nio.IntBuffer;

/**
 * Abstract class for vectors with integer precision.
 */
public abstract class VectorInt<V extends VectorInt<V>> extends Vector<V> {
	
	@Override
	public float length() {
		return MathUtils.sqrt(lengthSquared());
	}
	
	/**
	 * Calculates the squared length of this vector.
	 */
	abstract public int lengthSquared();
	
	/**
	 * Calculates the dot product of this vector with another vector.
	 * @return Dot product of this vector multiplied by another vector
	 */
	abstract public int dot(V vector);
	
	/**
	 * Stores the vector in a given buffer.
	 * @param buffer The buffer to store the vector data in
	 */
	abstract public void toBuffer(IntBuffer buffer);
	
}
