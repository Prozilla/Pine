package dev.prozilla.pine.common.math.vector;

import java.nio.IntBuffer;

/**
 * 4-dimensional vector with integer precision. GLSL equivalent to <code>ivec4</code>.
 */
public class Vector4i implements VectorInt<Vector4i> {
	
	public int x;
	public int y;
	public int z;
	public int w;
	
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
	
	/**
	 * Converts this vector to a string representation in the format "(x,y,z,w)".
	 */
	@Override
	public String toString() {
		return String.format("(%s,%s,%s,%s)", x, y, z, w);
	}
}
