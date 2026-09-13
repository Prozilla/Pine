package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.selection.WrapMode;
import org.jetbrains.annotations.NotNull;

import java.nio.IntBuffer;
import java.util.Objects;

/**
 * 3-dimensional vector with integer precision. GLSL equivalent to <code>ivec3</code>.
 */
public class Vector3i extends VectorInt<Vector3i> {
	
	public int x;
	public int y;
	public int z;
	
	/**
	 * Reusable temporary vector, to avoid repeatedly creating new instances in performance-critical contexts.
	 */
	public static final Vector3i temp = new Vector3i();
	
	/**
	 * Creates a default 3-dimensional vector with all values set to <code>0</code>.
	 */
	public Vector3i() {}
	
	public Vector3i(int xyz) {
		this.x = xyz;
		this.y = xyz;
		this.z = xyz;
	}
	
	public Vector3i(Vector3i vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}
	
	/**
	 * Creates a 3-dimensional vector with given values.
	 */
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public Vector3i set(int xyz) {
		this.x = xyz;
		this.y = xyz;
		this.z = xyz;
		return this;
	}
	
	@Override
	public Vector3i set(Vector3i vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		return this;
	}
	
	public Vector3i set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector4i expand(int w) {
		return new Vector4i(x, y, z, w);
	}
	
	public Vector2i shrink() {
		return new Vector2i(x, y);
	}
	
	public Vector3i add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	@Override
	public Vector3i add(Vector3i vector) {
		x += vector.x;
		y += vector.y;
		z += vector.z;
		return this;
	}
	
	public Vector3i subtract(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	@Override
	public Vector3i subtract(Vector3i vector) {
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
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
	
	public int dot(int x, int y, int z) {
		return this.x * x + this.y * y + this.z * z;
	}
	
	@Override
	public int dot(Vector3i vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}
	
	@Override
	public float distance(Vector3i vector) {
		return distance(vector.x, vector.y, vector.z);
	}
	
	public float distance(int x, int y, int z) {
		return MathUtils.sqrt(distanceSquared(x, y, z));
	}
	
	@Override
	public float distanceSquared(Vector3i vector) {
		return distanceSquared(vector.x, vector.y, vector.z);
	}
	
	public float distanceSquared(int x, int y, int z) {
		return MathUtils.square(x - this.x) + MathUtils.square(y - this.y) + MathUtils.square(z - this.z);
	}
	
	@Override
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}
	
	@Override
	public void toBuffer(IntBuffer buffer) {
		buffer.put(x).put(y).put(z);
		buffer.flip();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
	
	@Override
	public boolean equals(Vector3i vector) {
		return vector != null && vector.x == x && vector.y == y && vector.z == z;
	}
	
	@Override
	public Vector3i clone() {
		return new Vector3i(x, y, z);
	}
	
	@Override
	public Vector3i self() {
		return this;
	}
	
	/**
	 * Converts this vector to a string representation in the format "(x,y,z)".
	 */
	@Override
	public @NotNull String toString() {
		return String.format("(%s,%s,%s)", x, y, z);
	}
	
	/**
	 * Creates a new vector (0, 0, 0)
	 */
	public static Vector3i zero() {
		return new Vector3i(0, 0, 0);
	}
	
	/**
	 * Creates a new vector (1, 1, 1)
	 */
	public static Vector3i one() {
		return new Vector3i(1, 1, 1);
	}
	
	/**
	 * Returns a temporary vector with given values.
	 * Note that this temporary vector is a global instance, so avoid concurrent usage.
	 */
	public static Vector3i getTemp(int x, int y, int z) {
		temp.x = x;
		temp.y = y;
		temp.z = z;
		return temp;
	}
	
	public static class Parser extends dev.prozilla.pine.common.util.parser.Parser<Vector3i> {
		
		@Override
		public boolean parse(String input) {
			Integer[] integers = Vector.parseToIntegers(input);
			WrapMode wrapMode = WrapMode.REPEAT;
			
			return succeed(new Vector3i(
				wrapMode.getElement(0, integers),
				wrapMode.getElement(1, integers),
				wrapMode.getElement(2, integers)
			));
		}
		
	}
	
}
