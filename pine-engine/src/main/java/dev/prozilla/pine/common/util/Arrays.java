package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.array.ArrayUtils;
import dev.prozilla.pine.common.exception.InvalidArrayException;

import java.util.Objects;

/**
 * Static utility methods for checking certain conditions before operation on arrays.
 */
public class Arrays {
	
	/**
	 * Checks that the given arrays are disjunct and throws an {@link InvalidArrayException} if it is not.
	 * @param arrayA The first array
	 * @param arrayB The second array
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>arrayA</code> and <code>arrayB</code> are not disjunct.
	 */
	public static <E> void requireDisjunct(E[] arrayA, E[] arrayB) throws InvalidArrayException {
		requireDisjunct(arrayA, arrayB, null);
	}
	
	/**
	 * Checks that the given arrays are disjunct and throws a customized {@link InvalidArrayException} if it is not.
	 * @param arrayA The first array
	 * @param arrayB The second array
	 * @param message The message to be used in the event that an {@link InvalidArrayException} is thrown
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>arrayA</code> and <code>arrayB</code> are not disjunct.
	 */
	public static <E> void requireDisjunct(E[] arrayA, E[] arrayB, String message) throws InvalidArrayException {
		Objects.requireNonNull(arrayA, "array must not be null");
		Objects.requireNonNull(arrayB, "array must not be null");
		
		if (ArrayUtils.overlaps(arrayA, arrayB)) {
			throw new InvalidArrayException(message != null ? message : "arrays must be disjunct");
		}
	}
	
	/**
	 * Checks that the given array is not empty and throws an {@link InvalidArrayException} if it is.
	 * @param array The array to check
	 * @return <code>array</code> if it is not empty.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>array</code> is empty.
	 */
	public static <E> E[] requireNonEmpty(E[] array) throws InvalidArrayException {
		return requireNonEmpty(array, null);
	}
	
	/**
	 * Checks that the given array is not empty and throws a customized {@link InvalidArrayException} if it is.
	 * @param array The array to check
	 * @param message The message to be used in the event that an {@link InvalidArrayException} is thrown
	 * @return <code>array</code> if it is not empty.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>array</code> is empty.
	 */
	public static <E> E[] requireNonEmpty(E[] array, String message) throws InvalidArrayException {
		Objects.requireNonNull(array, "array must not be null");
		
		return requireMinLength(array, 1, message != null ? message : "length of array must be greater than 0");
	}
	
	/**
	 * Checks that the given array's length is greater than or equal to the minimum length and throws an {@link InvalidArrayException} if it is not.
	 * @param array The array to check
	 * @param minLength The minimum length of the array (inclusive)
	 * @return <code>array</code> if its length is greater or equal.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If length of <code>array</code> is less than the minimum length.
	 */
	public static <E> E[] requireMinLength(E[] array, int minLength) throws InvalidArrayException {
		return requireMinLength(array, minLength, null);
	}
	
	/**
	 * Checks that the given array's length is greater than or equal to the minimum length and throws a customized {@link InvalidArrayException} if it is not.
	 * @param array The array to check
	 * @param minLength The minimum length of the array (inclusive)
	 * @param message The message to be used in the event that an {@link InvalidArrayException} is thrown
	 * @return <code>array</code> if its length is greater or equal.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If length of <code>array</code> is less than the minimum length.
	 */
	public static <E> E[] requireMinLength(E[] array, int minLength, String message) throws InvalidArrayException {
		Objects.requireNonNull(array, "array must not be null");
		
		if (array.length < minLength) {
			throw new InvalidArrayException(message != null ? message : "length of array must be greater than or equal to " + minLength);
		} else {
			return array;
		}
	}
	
	/**
	 * Checks that the given array's length is less than or equal to the maximum length and throws an {@link InvalidArrayException} if it is not.
	 * @param array The array to check
	 * @param maxLength The maximum length of the array (inclusive)
	 * @return <code>array</code> if its length is less or equal.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If length of <code>array</code> is greater than the maximum length.
	 */
	public static <E> E[] requireMaxLength(E[] array, int maxLength) throws InvalidArrayException {
		return requireMinLength(array, maxLength, null);
	}
	
	/**
	 * Checks that the given array's length is less than or equal to the maximum length and throws a customized {@link InvalidArrayException} if it is not.
	 * @param array The array to check
	 * @param maxLength The maximum length of the array (inclusive)
	 * @param message The message to be used in the event that an {@link InvalidArrayException} is thrown
	 * @return <code>array</code> if its length is less or equal.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If length of <code>array</code> is greater than the maximum length.
	 */
	public static <E> E[] requireMaxLength(E[] array, int maxLength, String message) throws InvalidArrayException {
		Objects.requireNonNull(array, "array must not be null");
		
		if (array.length > maxLength) {
			throw new InvalidArrayException(message != null ? message : "length of array must be less than or equal to " + maxLength);
		} else {
			return array;
		}
	}
}
