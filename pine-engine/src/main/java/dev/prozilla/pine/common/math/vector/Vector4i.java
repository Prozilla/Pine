package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.selection.WrapMode;
import org.jetbrains.annotations.NotNull;

import java.nio.IntBuffer;
import java.util.Objects;

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
	public Vector4i() {}
	
	public Vector4i(int xyzw) {
		this.x = xyzw;
		this.y = xyzw;
		this.z = xyzw;
		this.w = xyzw;
	}
	
	public Vector4i(Vector4i vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.w = vector.w;
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
	
	@Override
	public Vector4i set(int xyzw) {
		this.x = xyzw;
		this.y = xyzw;
		this.z = xyzw;
		this.w = xyzw;
		return this;
	}
	
	@Override
	public Vector4i set(Vector4i vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		this.w = vector.w;
		return this;
	}
	
	public Vector4i set(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Vector3i shrink() {
		return new Vector3i(x, y, z);
	}
	
	public Vector4i add(int x, int y, int z, int w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}
	
	@Override
	public Vector4i add(Vector4i vector) {
		x += vector.x;
		y += vector.y;
		z += vector.z;
		w += vector.w;
		return this;
	}
	
	public Vector4i subtract(int x, int y, int z, int w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
		return this;
	}
	
	@Override
	public Vector4i subtract(Vector4i vector) {
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
		w -= vector.w;
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
	
	public int dot(int x, int y, int z, int w) {
		return this.x * x + this.y * y + this.z * z + this.w * w;
	}
	
	@Override
	public int dot(Vector4i vector) {
		return x * vector.x + y * vector.y + z * vector.z + w * vector.w;
	}
	
	@Override
	public float distance(Vector4i vector) {
		return distance(vector.x, vector.y, vector.z, vector.w);
	}
	
	public float distance(int x, int y, int z, int w) {
		return MathUtils.sqrt(distanceSquared(x, y, z, w));
	}
	
	@Override
	public float distanceSquared(Vector4i vector) {
		return distanceSquared(vector.x, vector.y, vector.z, vector.w);
	}
	
	public float distanceSquared(int x, int y, int z, int w) {
		return MathUtils.square(x - this.x) + MathUtils.square(y - this.y) + MathUtils.square(z - this.z) + MathUtils.square(w - this.w);
	}
	
	@Override
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0 && w == 0;
	}
	
	@Override
	public void toBuffer(IntBuffer buffer) {
		buffer.put(x).put(y).put(z).put(w);
		buffer.flip();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, w);
	}
	
	@Override
	public boolean equals(Vector4i vector) {
		return vector != null && vector.x == x && vector.y == y && vector.z == z && vector.w == w;
	}
	
	@Override
	public Vector4i clone() {
		return new Vector4i(x, y, z, w);
	}
	
	@Override
	public Vector4i self() {
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
	public static Vector4i zero() {
		return new Vector4i(0, 0, 0, 0);
	}
	
	/**
	 * Creates a new vector (1, 1, 1, 1)
	 */
	public static Vector4i one() {
		return new Vector4i(1, 1, 1, 1);
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
	
	public static class Parser extends dev.prozilla.pine.common.util.parser.Parser<Vector4i> {
		
		@Override
		public boolean parse(String input) {
			Integer[] integers = Vector.parseToIntegers(input);
			WrapMode wrapMode = WrapMode.REPEAT;
			
			return succeed(new Vector4i(
				wrapMode.getElement(0, integers),
				wrapMode.getElement(1, integers),
				wrapMode.getElement(2, integers),
				wrapMode.getElement(3, integers)
			));
		}
		
	}
	
}
