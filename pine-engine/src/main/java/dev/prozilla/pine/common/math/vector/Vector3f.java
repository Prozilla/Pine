package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.exception.InvalidStringException;
import dev.prozilla.pine.common.property.selection.WrapMode;
import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;
import java.util.Objects;

/**
 * 3-dimensional vector with floating point precision. GLSL equivalent to <code>vec3</code>.
 */
public class Vector3f extends VectorFloat<Vector3f> {
	
	public float x;
	public float y;
	public float z;
	
	/**
	 * Reusable temporary vector, to avoid repeatedly creating new instances in performance-critical contexts.
	 */
	public static final Vector3f temp = new Vector3f();
	
	/**
	 * Creates a default 3-dimensional vector with all values set to <code>0f</code>.
	 */
	public Vector3f() {
		this(0f, 0f, 0f);
	}
	
	/**
	 * Creates a 3-dimensional vector with given values.
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f set(float xyz) {
		return set(xyz, xyz, xyz);
	}
	
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector3f add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	@Override
	public Vector3f add(Vector3f vector3f) {
		x += vector3f.x;
		y += vector3f.y;
		z += vector3f.z;
		return this;
	}
	
	@Override
	public Vector3f subtract(Vector3f vector) {
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
		return this;
	}
	
	@Override
	public Vector3f scale(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}
	
	@Override
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	@Override
	public float dot(Vector3f vector3f) {
		return x * vector3f.x + y * vector3f.y + z * vector3f.z;
	}
	
	@Override
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}
	
	@Override
	public void toBuffer(FloatBuffer buffer) {
		buffer.put(x).put(y).put(z);
		buffer.flip();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
	
	@Override
	public boolean equals(Vector3f vector) {
		return vector != null && vector.x == x && vector.y == y && vector.z == z;
	}
	
	@Override
	public Vector3f clone() {
		return new Vector3f(x, y, z);
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y,z)".
	 */
	@Override
	public @NotNull String toString() {
		return String.format("(%s,%s,%s)", x, y, z);
	}
	
	/**
	 * @deprecated Replaced by {@link Parser} as of 2.0.2
	 */
	@Deprecated
	public static Vector3f parse(String input) throws InvalidStringException {
		return new Parser().read(input);
	}
	
	/**
	 * Creates a new vector (1, 1, 1)
	 */
	public static Vector3f one() {
		return new Vector3f(1, 1, 1);
	}
	
	/**
	 * Returns a temporary vector with given values.
	 * Note that this temporary vector is a global instance, so avoid concurrent usage.
	 */
	public static Vector3f getTemp(float x, float y, float z) {
		temp.x = x;
		temp.y = y;
		temp.z = z;
		return temp;
	}
	
	public static class Parser extends dev.prozilla.pine.common.util.Parser<Vector3f> {
		
		@Override
		public boolean parse(String input) {
			Float[] floats = Vector.parseToFloats(input);
			WrapMode wrapMode = WrapMode.REPEAT;
			
			return succeed(new Vector3f(
				wrapMode.getElement(0, floats),
				wrapMode.getElement(1, floats),
				wrapMode.getElement(2, floats)
			));
		}
		
	}
	
}
