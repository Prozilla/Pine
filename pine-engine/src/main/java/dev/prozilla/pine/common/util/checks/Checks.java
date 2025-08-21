package dev.prozilla.pine.common.util.checks;

import dev.prozilla.pine.common.exception.InvalidArrayException;
import dev.prozilla.pine.common.exception.InvalidNumberException;
import dev.prozilla.pine.common.exception.InvalidObjectException;
import dev.prozilla.pine.common.exception.InvalidStringException;
import dev.prozilla.pine.common.util.ArrayUtils;
import org.gradle.internal.impldep.javax.annotation.Nonnull;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Objects;

/**
 * Static utility methods for throwing exceptions if certain conditions are not met at runtime.
 */
public final class Checks {
	
	private Checks() {}
	
	// Objects
	
	public static <O> O isNotNull(O object) throws InvalidObjectException {
		return isNotNull(object, null);
	}
	
	@Contract("null, _ -> fail")
	public static <O> O isNotNull(O object, String name) throws InvalidObjectException {
		if (object == null) {
			throw new InvalidObjectException(Objects.requireNonNullElse(name, "object") + " must not be null");
		}
		return object;
	}
	
	// Arrays
	
	/**
	 * Checks that the given arrays are disjunct and throws an {@link InvalidArrayException} if it is not.
	 * @param arrayA The first array
	 * @param arrayB The second array
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>arrayA</code> and <code>arrayB</code> are not disjunct.
	 */
	public static <E> void areDisjunct(E[] arrayA, E[] arrayB) throws InvalidArrayException {
		areDisjunct(arrayA, arrayB, null);
	}
	
	/**
	 * Checks that the given arrays are disjunct and throws a customized {@link InvalidArrayException} if it is not.
	 * @param arrayA The first array
	 * @param arrayB The second array
	 * @param message The message to be used in the event that an {@link InvalidArrayException} is thrown
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>arrayA</code> and <code>arrayB</code> are not disjunct.
	 */
	public static <E> void areDisjunct(E[] arrayA, E[] arrayB, String message) throws InvalidArrayException {
		Checks.isNotNull(arrayA, "arrayA");
		Checks.isNotNull(arrayB, "arrayB");
		
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
	public static <E> E[] isNotEmpty(E[] array) throws InvalidArrayException {
		return isNotEmpty(array, null);
	}
	
	/**
	 * Checks that the given array is not empty and throws a customized {@link InvalidArrayException} if it is.
	 * @param array The array to check
	 * @param message The message to be used in the event that an {@link InvalidArrayException} is thrown
	 * @return <code>array</code> if it is not empty.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>array</code> is empty.
	 */
	public static <E> E[] isNotEmpty(E[] array, String message) throws InvalidArrayException {
		Checks.isNotNull(array, "array");
		return hasMinLength(array, 1, message != null ? message : "length of array must be greater than 0");
	}
	
	/**
	 * Checks that the given array's length is greater than or equal to the minimum length and throws an {@link InvalidArrayException} if it is not.
	 * @param array The array to check
	 * @param minLength The minimum length of the array (inclusive)
	 * @return <code>array</code> if its length is greater or equal.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If length of <code>array</code> is less than the minimum length.
	 */
	public static <E> E[] hasMinLength(E[] array, int minLength) throws InvalidArrayException {
		return hasMinLength(array, minLength, null);
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
	public static <E> E[] hasMinLength(E[] array, int minLength, String message) throws InvalidArrayException {
		Checks.isNotNull(array, "array");
		
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
	public static <E> E[] hasMaxLength(E[] array, int maxLength) throws InvalidArrayException {
		return hasMaxLength(array, maxLength, null);
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
	public static <E> E[] hasMaxLength(E[] array, int maxLength, String message) throws InvalidArrayException {
		Checks.isNotNull(array, "array");
		
		if (array.length > maxLength) {
			throw new InvalidArrayException(message != null ? message : "length of array must be less than or equal to " + maxLength);
		} else {
			return array;
		}
	}
	
	/**
	 * Checks that the given array has a given length and throws an {@link InvalidArrayException} if it does not.
	 * @param array The array to check
	 * @param length The required length of the array
	 * @return <code>array</code> if it matches the required length.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>array</code> matches the required length.
	 */
	public static <E> E[] hasLength(E[] array, int length) throws InvalidArrayException {
		return hasLength(array, length, null);
	}
	
	/**
	 * Checks that the given array has a given length and throws a customized {@link InvalidArrayException} if it does not.
	 * @param array The array to check
	 * @param length The required length of the array
	 * @param message The message to be used in the event that an {@link InvalidArrayException} is thrown
	 * @return <code>array</code> if it matches the required length.
	 * @param <E> Type of the array elements
	 * @throws InvalidArrayException If <code>array</code> matches the required length.
	 */
	public static <E> E[] hasLength(E[] array, int length, String message) throws InvalidArrayException {
		Checks.isNotNull(array, "array");
		
		if (array.length != length) {
			throw new InvalidArrayException(message != null ? message : "length of array must be " + length);
		} else {
			return array;
		}
	}
	
	// Strings
	
	/**
	 * Checks that the given string is not blank and throws an {@link InvalidStringException} if it is not.
	 * @param string The string to check
	 * @return <code>string</code> if it is not blank.
	 * @throws InvalidStringException If <code>string</code> is blank.
	 */
	public static String isNotBlank(@Nonnull String string) throws InvalidStringException {
		return isNotBlank(string, null);
	}
	
	/**
	 * Checks that the given string is not blank and throws a customized {@link InvalidStringException} if it is.
	 * @param string The string to check
	 * @param message The message to be used in the event that an {@link InvalidStringException} is thrown
	 * @return <code>string</code> if it is not blank.
	 * @throws InvalidStringException If <code>string</code> is blank.
	 */
	public static String isNotBlank(@Nonnull String string, String message) throws InvalidStringException {
		if (string.isBlank()) {
			throw new InvalidStringException(message != null ? message : "string must not be blank");
		} else {
			return string;
		}
	}
	
	/**
	 * Checks that the given string is not empty and throws an {@link InvalidStringException} if it is not.
	 * @param string The string to check
	 * @return <code>string</code> if it is not empty.
	 * @throws InvalidStringException If <code>string</code> is empty.
	 */
	public static String isNotEmpty(@Nonnull String string) throws InvalidStringException {
		return isNotEmpty(string, null);
	}
	
	/**
	 * Checks that the given string is not empty and throws a customized {@link InvalidStringException} if it is.
	 * @param string The string to check
	 * @param message The message to be used in the event that an {@link InvalidStringException} is thrown
	 * @return <code>string</code> if it is not empty.
	 * @throws InvalidStringException If <code>string</code> is empty.
	 */
	public static String isNotEmpty(@Nonnull String string, String message) throws InvalidStringException {
		if (string.isEmpty()) {
			throw new InvalidStringException(message != null ? message : "string must not be empty");
		} else {
			return string;
		}
	}
	
	/**
	 * Checks that the given string starts with a prefix and throws an {@link InvalidStringException} if it doesn't.
	 * @param string The string to check
	 * @param prefix The required prefix of the string
	 * @return <code>string</code> if it starts with a given prefix.
	 * @throws InvalidStringException If <code>string</code> does not start with a given prefix.
	 */
	public static String hasPrefix(@Nonnull String string, @Nonnull String prefix) throws InvalidStringException {
		return hasPrefix(string, prefix, null);
	}
	
	/**
	 * Checks that the given string starts with a prefix and throws a customized {@link InvalidStringException} if it doesn't.
	 * @param string The string to check
	 * @param prefix The required prefix of the string
	 * @param message The message to be used in the event that an {@link InvalidStringException} is thrown
	 * @return <code>string</code> if it starts with a given prefix.
	 * @throws InvalidStringException If <code>string</code> does not start with a given prefix.
	 */
	public static String hasPrefix(@Nonnull String string, @Nonnull String prefix, String message) throws InvalidStringException {
		if (!string.startsWith(prefix)) {
			throw new InvalidStringException(message != null ? message : "string must start with prefix: " + prefix);
		} else {
			return string;
		}
	}
	
	/**
	 * Checks that the given string ends with a suffix and throws an {@link InvalidStringException} if it doesn't.
	 * @param string The string to check
	 * @param suffix The required suffix of the string
	 * @return <code>string</code> if it ends with a given suffix.
	 * @throws InvalidStringException If <code>string</code> does not end with a given suffix.
	 */
	public static String hasSuffix(@Nonnull String string, @Nonnull String suffix) throws InvalidStringException {
		return hasSuffix(string, suffix, null);
	}
	
	/**
	 * Checks that the given string ends with a suffix and throws a customized {@link InvalidStringException} if it doesn't.
	 * @param string The string to check
	 * @param suffix The required suffix of the string
	 * @param message The message to be used in the event that an {@link InvalidStringException} is thrown
	 * @return <code>string</code> if it ends with a given suffix.
	 * @throws InvalidStringException If <code>string</code> does not end with a given suffix.
	 */
	public static String hasSuffix(@Nonnull String string, @Nonnull String suffix, String message) throws InvalidStringException {
		if (!string.endsWith(suffix)) {
			throw new InvalidStringException(message != null ? message : "string must end with suffix: " + suffix);
		} else {
			return string;
		}
	}
	
	// Numbers
	
	/**
	 * Checks that the given number is a positive number and throws an {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param strict Whether to throw an exception if <code>number</code> is <code>0f</code>
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static float isPositive(float number, boolean strict) throws InvalidNumberException {
		return isPositive(number, strict, null);
	}
	
	/**
	 * Checks that the given number is a positive number and throws a customized {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param strict Whether to throw an exception if <code>number</code> is <code>0f</code>
	 * @param message The message to be used in the event that an {@link InvalidNumberException} is thrown
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static float isPositive(float number, boolean strict, String message) throws InvalidNumberException {
		if (number < 0 || (number == 0 && strict)) {
			throw new InvalidNumberException(message != null ? message : "number must be positive");
		} else {
			return number;
		}
	}
	
	/**
	 * Checks that the given number is a positive number and throws an {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param strict Whether to throw an exception if <code>number</code> is <code>0</code>
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static int isPositive(int number, boolean strict) throws InvalidNumberException {
		return isPositive(number, strict, null);
	}
	
	/**
	 * Checks that the given number is a positive number and throws a customized {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param strict Whether to throw an exception if <code>number</code> is <code>0</code>
	 * @param message The message to be used in the event that an {@link InvalidNumberException} is thrown
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static int isPositive(int number, boolean strict, String message) throws InvalidNumberException {
		if (number < 0 || (number == 0 && strict)) {
			throw new InvalidNumberException(message != null ? message : "number must be positive");
		} else {
			return number;
		}
	}
	
	/**
	 * Checks that the given number is in the given range and throws an {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param min Lower bound (inclusive)
	 * @param max Upper bound (inclusive)
	 * @return <code>number</code> if it is in the range.
	 * @throws InvalidNumberException If <code>number</code> is not in the range.
	 */
	public static int isInRange(int number, int min, int max) throws InvalidNumberException {
		return isInRange(number, min, max, null);
	}
	
	/**
	 * Checks that the given number is in the given range and throws a customized {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param min Lower bound (inclusive)
	 * @param max Upper bound (inclusive)
	 * @param message The message to be used in the event that an {@link InvalidNumberException} is thrown
	 * @return <code>number</code> if it is in the range.
	 * @throws InvalidNumberException If <code>number</code> is not in the range.
	 */
	public static int isInRange(int number, int min, int max, String message) throws InvalidNumberException {
		if (number < min || number > max) {
			throw new InvalidNumberException(message != null ? message : String.format("number must be between %s and %s", min, max));
		} else {
			return number;
		}
	}
	
	public static <E> ArrayChecks<E> array(E[] value, String name) {
		return new ArrayChecks<>(value, name);
	}
	
	public static <E> CollectionChecks<E> collection(Collection<E> value, String name) {
		return new CollectionChecks<>(value, name);
	}
	
	public static IntChecks integer(int value, String name) {
		return new IntChecks(value, name);
	}
	
	public static ObjectChecks object(Object value, String name) {
		return new ObjectChecks(value, name);
	}
	
	public static StringChecks string(String value, String name) {
		return new StringChecks(value, name);
	}
	
}
