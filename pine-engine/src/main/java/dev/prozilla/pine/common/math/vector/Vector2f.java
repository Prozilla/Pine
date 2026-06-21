package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.selection.WrapMode;
import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;
import java.util.Objects;

/**
 * 2-dimensional vector with floating point precision. GLSL equivalent to <code>vec2</code>.
 */
public class Vector2f extends VectorFloat<Vector2f> {
	
	public float x;
	public float y;
	
	/**
	 * Reusable temporary vector, to avoid repeatedly creating new instances in performance-critical contexts.
	 */
	public static final Vector2f temp = new Vector2f();
	
	/**
	 * Creates a default 3-dimensional vector with all values set to <code>0f</code>.
	 */
	public Vector2f() {}
	
	public Vector2f(float xy) {
		this.x = xy;
		this.y = xy;
	}
	
	/**
	 * Creates a clone of a 3-dimensional vector.
	 */
	public Vector2f(Vector2f vector) {
		this.x = vector.x;
		this.y = vector.y;
	}
	
	/**
	 * Converts a 3-dimensional vector with integer precision to a vector with floating point precision.
	 */
	public Vector2f(Vector2i vector) {
		this.x = vector.x;
		this.y = vector.y;
	}
	
	/**
	 * Creates a 3-dimensional vector with given values.
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Vector2f set(float xyz) {
		this.x = xyz;
		this.y = xyz;
		return this;
	}
	
	@Override
	public Vector2f set(Vector2f vector) {
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}
	
	public Vector2f set(Vector2i vector) {
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}
	
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector3f expand(float z) {
		return new Vector3f(x, y, z);
	}
	
	public Vector2f add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	@Override
	public Vector2f add(Vector2f vector) {
		x += vector.x;
		y += vector.y;
		return this;
	}
	
	public Vector2f subtract(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	@Override
	public Vector2f subtract(Vector2f vector) {
		x -= vector.x;
		y -= vector.y;
		return this;
	}
	
	@Override
	public Vector2f scale(float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	@Override
	public float lengthSquared() {
		return x * x + y * y;
	}
	
	public float dot(float x, float y) {
		return this.x * x + this.y * y;
	}
	
	@Override
	public float dot(Vector2f vector) {
		return x * vector.x + y * vector.y;
	}
	
	@Override
	public float distance(Vector2f vector) {
		return distance(vector.x, vector.y);
	}
	
	public float distance(float x, float y) {
		return MathUtils.sqrt(distanceSquared(x, y));
	}
	
	@Override
	public float distanceSquared(Vector2f vector) {
		return distanceSquared(vector.x, vector.y);
	}
	
	public float distanceSquared(float x, float y) {
		return MathUtils.square(x - this.x) + MathUtils.square(y - this.y);
	}
	
	@Override
	public boolean isZero() {
		return x == 0 && y == 0;
	}
	
	@Override
	public void toBuffer(FloatBuffer buffer) {
		buffer.put(x).put(y);
		buffer.flip();
	}
	
	public Direction toDirection() {
		return Direction.fromFloatVector(this);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public boolean equals(Vector2f vector) {
		return vector != null && vector.x == x && vector.y == y;
	}
	
	@Override
	public Vector2f clone() {
		return new Vector2f(x, y);
	}
	
	@Override
	public Vector2f self() {
		return this;
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y)".
	 */
	@Override
	public @NotNull String toString() {
		return String.format("(%s,%s)", x, y);
	}
	
	/**
	 * Creates a new vector (0, 0)
	 */
	public static Vector2f zero() {
		return new Vector2f(0, 0);
	}
	
	/**
	 * Creates a new vector (1, 1)
	 */
	public static Vector2f one() {
		return new Vector2f(1, 1);
	}
	
	/**
	 * Creates a new vector (0, 1)
	 */
	public static Vector2f up() {
		return new Vector2f(0, 1);
	}
	
	/**
	 * Creates a new vector (0, -1)
	 */
	public static Vector2f down() {
		return new Vector2f(0, -1);
	}
	
	/**
	 * Creates a new vector (-1, 0)
	 */
	public static Vector2f left() {
		return new Vector2f(-1, 0);
	}
	
	/**
	 * Creates a new vector (1, 0)
	 */
	public static Vector2f right() {
		return new Vector2f(1, 0);
	}
	
	/**
	 * Returns a temporary vector with given values.
	 * Note that this temporary vector is a global instance, so avoid concurrent usage.
	 */
	public static Vector2f getTemp(float x, float y) {
		temp.x = x;
		temp.y = y;
		return temp;
	}
	
	public static class Parser extends dev.prozilla.pine.common.util.parser.Parser<Vector2f> {
		
		@Override
		public boolean parse(String input) {
			Float[] floats = Vector.parseToFloats(input);
			WrapMode wrapMode = WrapMode.REPEAT;
			
			return succeed(new Vector2f(
				wrapMode.getElement(0, floats),
				wrapMode.getElement(1, floats)
			));
		}
		
	}
	
}
