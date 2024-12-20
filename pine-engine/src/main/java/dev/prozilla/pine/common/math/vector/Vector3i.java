package dev.prozilla.pine.common.math.vector;

import java.nio.IntBuffer;

/**
 * 3-dimensional vector with integer precision. GLSL equivalent to <code>ivec3</code>.
 */
public class Vector3i implements VectorInt<Vector3i> {
	
	public int x;
	public int y;
	public int z;
	
	/**
	 * Creates a default 3-dimensional vector with all values set to <code>0</code>.
	 */
	public Vector3i() {
		this(0, 0, 0);
	}
	
	/**
	 * Creates a 3-dimensional vector with given values.
	 */
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3i add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	@Override
	public Vector3i add(Vector3i vector3i) {
		x += vector3i.x;
		y += vector3i.y;
		z += vector3i.z;
		return this;
	}
	
	@Override
	public Vector3i scale(float scalar) {
		x = Math.round(x * scalar);
		y = Math.round(y * scalar);
		z = Math.round(z * scalar);
		return this;
	}
	
	@Override
	public int lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	@Override
	public int dot(Vector3i vector3i) {
		return x * vector3i.x + y * vector3i.y + z * vector3i.z;
	}
	
	@Override
	public void toBuffer(IntBuffer buffer) {
		buffer.put(x).put(y).put(z);
		buffer.flip();
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y,z)".
	 */
	@Override
	public String toString() {
		return String.format("(%s,%s,%s)", x, y, z);
	}
}
