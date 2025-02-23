package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.exception.InvalidStringException;

import java.nio.IntBuffer;

/**
 * 4-dimensional vector with integer precision. GLSL equivalent to <code>ivec4</code>.
 */
public class Vector4i extends VectorInt<Vector4i> {
	
	public int x;
	public int y;
	public int z;
	public int w;
	
	/**
	 * Reusable temporary vector, to avoid repeatedly creating new instances in performance-critical contexts.
	 */
	public static final Vector4i temp = new Vector4i();
	
	/**
	 * Creates a default 4-dimensional vector with all values set to <code>0</code>.
	 */
	public Vector4i() {
		this(0, 0, 0, 0);
	}
	
	/**
	 * Creates a 4-dimensional vector with given values.
	 */
	public Vector4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4i add(int x, int y, int z, int w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}
	
	@Override
	public Vector4i add(Vector4i vector4i) {
		x += vector4i.x;
		y += vector4i.y;
		z += vector4i.z;
		w += vector4i.w;
		return this;
	}
	
	@Override
	public Vector4i scale(float scalar) {
		x = Math.round(x * scalar);
		y = Math.round(y * scalar);
		z = Math.round(z * scalar);
		w = Math.round(w * scalar);
		return this;
	}
	
	@Override
	public int lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}
	
	@Override
	public int dot(Vector4i vector4i) {
		return x * vector4i.x + y * vector4i.y + z * vector4i.z + w * vector4i.w;
	}
	
	@Override
	public void toBuffer(IntBuffer buffer) {
		buffer.put(x).put(y).put(z).put(w);
		buffer.flip();
	}
	
	@Override
	public boolean equals(Vector4i vector) {
		return vector.x == x && vector.y == y && vector.z == z && vector.w == w;
	}
	
	@Override
	public Vector4i clone() {
		return new Vector4i(x, y, z, w);
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y,z,w)".
	 */
	@Override
	public String toString() {
		return String.format("(%s,%s,%s,%s)", x, y, z, w);
	}
	
	public static Vector4i parse(String input) throws InvalidStringException {
		Integer[] integers = Vector.parseToIntegers(input, 4);
		return new Vector4i(integers[0], integers[1], integers[2], integers[3]);
	}
	
	/**
	 * Returns a temporary vector with given values.
	 * Note that this temporary vector is a global instance, so avoid concurrent usage.
	 */
	public static Vector4i getTemp(int x, int y, int z, int w) {
		temp.x = x;
		temp.y = y;
		temp.z = z;
		temp.w = w;
		return temp;
	}
}
