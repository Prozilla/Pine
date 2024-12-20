package dev.prozilla.pine.common.math.vector;

import java.nio.IntBuffer;

/**
 * 2-dimensional vector with integer precision. GLSL equivalent to <code>ivec2</code>.
 */
public class Vector2i implements VectorInt<Vector2i> {
	
	public int x;
	public int y;
	
	/**
	 * Creates a default 2-dimensional vector with all values set to <code>0</code>.
	 */
	public Vector2i() {
		this(0, 0);
	}
	
	/**
	 * Creates a 2-dimensional vector with given values.
	 */
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	@Override
	public Vector2i add(Vector2i vector2i) {
		x += vector2i.x;
		y += vector2i.y;
		return this;
	}
	
	@Override
	public Vector2i scale(float scalar) {
		x = Math.round(x * scalar);
		y = Math.round(y * scalar);
		return this;
	}
	
	@Override
	public int lengthSquared() {
		return x * x + y * y;
	}
	
	@Override
	public int dot(Vector2i vector2i) {
		return x * vector2i.x + y * vector2i.y;
	}
	
	@Override
	public void toBuffer(IntBuffer buffer) {
		buffer.put(x).put(y);
		buffer.flip();
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y)".
	 */
	@Override
	public String toString() {
		return String.format("(%s,%s)", x, y);
	}
}
