package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.exception.InvalidStringException;

import java.nio.IntBuffer;

/**
 * 2-dimensional vector with integer precision. GLSL equivalent to <code>ivec2</code>.
 */
public class Vector2i extends VectorInt<Vector2i> {
	
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
	
	@Override
	public boolean equals(Vector2i vector) {
		return vector.x == x && vector.y == y;
	}
	
	@Override
	public Vector2i clone() {
		return new Vector2i(x, y);
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y)".
	 */
	@Override
	public String toString() {
		return String.format("(%s,%s)", x, y);
	}
	
	public static Vector2i parse(String input) throws InvalidStringException {
		Integer[] integers = Vector.parseToIntegers(input, 2);
		return new Vector2i(integers[0], integers[1]);
	}
}
