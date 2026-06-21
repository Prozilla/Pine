package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.selection.WrapMode;
import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;
import java.util.Objects;

/**
 * 4-dimensional vector with floating point precision. GLSL equivalent to <code>vec4</code>.
 */
public class Vector4f extends VectorFloat<Vector4f> {
	
	public float x;
	public float y;
	public float z;
	public float w;
	
	/**
	 * Reusable temporary vector, to avoid repeatedly creating new instances in performance-critical contexts.
	 */
	public static final Vector4f temp = new Vector4f();
	
	/**
	 * Creates a default 4-dimensional vector with all values set to <code>0f</code>.
	 */
	public Vector4f() {}
	
	public Vector4f(float xyzw) {
		this.x = xyzw;
		this.y = xyzw;
		this.z = xyzw;
		this.w = xyzw;
	}
	
	/**
	 * Creates a clone of a 4-dimensional vector.
	 */
	public Vector4f(Vector4f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.w = vector.w;
	}
	
	/**
	 * Converts a 4-dimensional vector with integer precision to a vector with floating point precision.
	 */
	public Vector4f(Vector4i vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.w = vector.w;
	}
	
	/**
	 * Creates a 4-dimensional vector with given values.
	 */
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	@Override
	public Vector4f set(float xyzw) {
		this.x = xyzw;
		this.y = xyzw;
		this.z = xyzw;
		this.w = xyzw;
		return this;
	}
	
	@Override
	public Vector4f set(Vector4f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.w = vector.w;
		return this;
	}
	
	public Vector4f set(Vector4i vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		return this;
	}
	
	public Vector4f set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Vector3f shrink() {
		return new Vector3f(x, y, z);
	}
	
	public Vector4f add(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}
	
	@Override
	public Vector4f add(Vector4f vector) {
		x += vector.x;
		y += vector.y;
		z += vector.z;
		w += vector.w;
		return this;
	}
	
	public Vector4f subtract(float x, float y, float z, float w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
		return this;
	}
	
	@Override
	public Vector4f subtract(Vector4f vector) {
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
		w -= vector.w;
		return this;
	}
	
	@Override
	public Vector4f scale(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;
		return this;
	}
	
	@Override
	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}
	
	public float dot(float x, float y, float z, float w) {
		return this.x * x + this.y * y + this.z * z + this.w * w;
	}
	
	@Override
	public float dot(Vector4f vector) {
		return x * vector.x + y * vector.y + z * vector.z + w * vector.w;
	}
	
	@Override
	public float distance(Vector4f vector) {
		return distance(vector.x, vector.y, vector.z, vector.w);
	}
	
	public float distance(float x, float y, float z, float w) {
		return MathUtils.sqrt(distanceSquared(x, y, z, w));
	}
	
	@Override
	public float distanceSquared(Vector4f vector) {
		return distanceSquared(vector.x, vector.y, vector.z, vector.w);
	}
	
	public float distanceSquared(float x, float y, float z, float w) {
		return MathUtils.square(x - this.x) + MathUtils.square(y - this.y) + MathUtils.square(z - this.z) + MathUtils.square(w - this.w);
	}
	
	@Override
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0 && w == 0;
	}
	
	@Override
	public void toBuffer(FloatBuffer buffer) {
		buffer.put(x).put(y).put(z).put(w);
		buffer.flip();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, w);
	}
	
	@Override
	public boolean equals(Vector4f vector) {
		return vector != null && vector.x == x && vector.y == y && vector.z == z && vector.w == w;
	}
	
	@Override
	public Vector4f clone() {
		return new Vector4f(x, y, z, w);
	}
	
	@Override
	public Vector4f self() {
		return this;
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y,z,w)".
	 */
	@Override
	public @NotNull String toString() {
		return String.format("(%s,%s,%s,%s)", x, y, z, w);
	}
	
	/**
	 * Creates a new vector (0, 0, 0, 0)
	 */
	public static Vector4f zero() {
		return new Vector4f(0, 0, 0, 0);
	}
	
	/**
	 * Creates a new vector (1, 1, 1, 1)
	 */
	public static Vector4f one() {
		return new Vector4f(1, 1, 1, 1);
	}
	
	/**
	 * Returns a temporary vector with given values.
	 * Note that this temporary vector is a global instance, so avoid concurrent usage.
	 */
	public static Vector4f getTemp(float x, float y, float z, float w) {
		temp.x = x;
		temp.y = y;
		temp.z = z;
		temp.w = w;
		return temp;
	}
	
	public static class Parser extends dev.prozilla.pine.common.util.parser.Parser<Vector4f> {
		
		@Override
		public boolean parse(String input) {
			Float[] floats = Vector.parseToFloats(input);
			WrapMode wrapMode = WrapMode.REPEAT;
			
			return succeed(new Vector4f(
				wrapMode.getElement(0, floats),
				wrapMode.getElement(1, floats),
				wrapMode.getElement(2, floats),
				wrapMode.getElement(3, floats)
			));
		}
		
	}
	
}
