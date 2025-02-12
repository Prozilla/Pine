package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.exception.InvalidArrayException;
import dev.prozilla.pine.common.exception.InvalidStringException;
import dev.prozilla.pine.common.util.Arrays;
import dev.prozilla.pine.common.util.Strings;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.Function;

/**
 * Abstract class for vectors.
 */
public abstract class Vector<V extends Vector<V>> implements Printable, Cloneable<V> {
	
	/**
	 * Calculates the length of this vector.
	 */
	abstract public float length();
	
	/**
	 * Normalizes this vector.
	 * @return Self.
	 */
	public V normalize() {
		return divide(length());
	}
	
	/**
	 * Adds another vector to this vector.
	 * @return Self
	 */
	abstract public V add(V vector);
	
	/**
	 * Negates this vector.
	 * @return Self
	 */
	public V negate() {
		return scale(-1f);
	}
	
	/**
	 * Subtracts another vector from this vector.
	 * @return Self
	 */
	public V subtract(V vector) {
		return add(vector.negate());
	}
	
	/**
	 * Scales this vector by a scalar.
	 * @return Self
	 */
	abstract public V scale(float scalar);
	
	/**
	 * Divides this vector by a scalar.
	 * @return Self
	 */
	public V divide(float scalar) {
		return scale(1f / scalar);
	}
	
	/**
	 * Calculates a linear interpolation between this vector with another vector.
	 * @param alpha The alpha value, in the range of <code>0f</code> and <code>1f</code>
	 * @return Self
	 */
	public V lerp(V vector, float alpha) {
		return scale(1f - alpha).add(vector.scale(alpha));
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		V vector = (V)object;
		return equals(vector);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	abstract public boolean equals(V vector);
	
	abstract public V clone();
	
	/**
	 * Converts this vector to a string representation.
	 */
	@Override
	abstract public String toString();
	
	protected static <T> T[] parseToNumbers(String input, Function<String, T> parser, Class<T> type) throws InvalidStringException {
		Objects.requireNonNull(input, "input must not be null");
		Strings.requirePrefix(input, "(");
		Strings.requireSuffix(input, ")");
		
		String[] strings = input.substring(1, input.length() - 1).split(",");
		T[] numbers = (T[])Array.newInstance(type, strings.length);
		
		for (int i = 0; i < strings.length; i++) {
			numbers[i] = parser.apply(strings[i].trim());
		}
		return numbers;
	}
	
	protected static Float[] parseToFloats(String input, int count) throws InvalidStringException, InvalidArrayException {
		return Arrays.requireLength(parseToFloats(input), count);
	}
	
	protected static Float[] parseToFloats(String input) throws InvalidStringException {
		return parseToNumbers(input, Float::parseFloat, Float.class);
	}
	
	protected static Integer[] parseToIntegers(String input, int count) throws InvalidStringException, InvalidArrayException {
		return Arrays.requireLength(parseToIntegers(input), count);
	}
	
	protected static Integer[] parseToIntegers(String input) throws InvalidStringException {
		return parseToNumbers(input, Integer::parseInt, Integer.class);
	}
}
