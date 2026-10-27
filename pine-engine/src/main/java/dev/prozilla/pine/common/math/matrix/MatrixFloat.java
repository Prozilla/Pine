package dev.prozilla.pine.common.math.matrix;

import dev.prozilla.pine.common.Transferable;
import dev.prozilla.pine.common.math.vector.VectorFloat;
import dev.prozilla.pine.common.util.checks.Checks;

import java.nio.FloatBuffer;

public abstract class MatrixFloat<V extends VectorFloat<V, M>, M extends MatrixFloat<V, M>> implements Transferable<M> {
	
	/**
	 * Sets this matrix to the identity matrix.
	 * @return This matrix.
	 */
	public abstract M setIdentity();
	
	/**
	 * Inverts this matrix.
	 * @return This matrix.
	 */
	public abstract M invert();
	
	/**
	 * Calculates the determinant of this matrix.
	 * @return The determinant of this matrix.
	 */
	public abstract float getDeterminant();
	
	/**
	 * Copies the values from a given matrix into this matrix.
	 * @param other The matrix to copy the values from
	 * @return This matrix.
	 */
	public abstract M set(M other);
	
	/**
	 * Adds a given matrix to this matrix.
	 * @param other The matrix to add to this matrix.
	 * @return This matrix.
	 */
	public abstract M add(M other);
	
	/**
	 * Subtracts a given matrix from this matrix.
	 * @param other The matrix to subtract from this matrix.
	 * @return This matrix.
	 */
	public abstract M subtract(M other);
	
	/**
	 * Negates this matrix.
	 * @return This matrix.
	 */
	public M negate() {
		return multiply(-1);
	}
	
	/**
	 * Divides this matrix by a given scalar.
	 * @param scalar The scalar to divide this matrix with.
	 * @return This matrix.
	 */
	public M negate(float scalar) {
		return multiply(1f / scalar);
	}
	
	/**
	 * Multiplies this matrix with a given scalar.
	 * @param scalar The scalar to multiply this matrix with.
	 * @return This matrix.
	 */
	public abstract M multiply(float scalar);
	
	/**
	 * Multiplies this matrix with a given vector.
	 * @param vector The vector to multiply this matrix with
	 * @return {@code vector}.
	 */
	public abstract V multiply(V vector);
	
	/**
	 * Multiplies this matrix with a given matrix.
	 * @param other The matrix to multiply this matrix with
	 * @return This matrix.
	 */
	public abstract M multiply(M other);
	
	/**
	 * Transposes this matrix.
	 * @return This matrix.
	 */
	public abstract M transpose();
	
	/**
	 * Stores this matrix in a given Buffer.
	 * @param buffer The buffer to store the matrix data in
	 */
	public abstract void toBuffer(FloatBuffer buffer);
	
	@Override
	public void transmit(M target) {
		Checks.isNotNull(target, "target");
		target.receive(self());
	}
	
	@Override
	public void receive(M source) {
		Checks.isNotNull(source, "source");
		set(source);
	}
	
	@Override
	public abstract M clone();
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		M matrix = (M)object;
		return equals(matrix);
	}
	
}
