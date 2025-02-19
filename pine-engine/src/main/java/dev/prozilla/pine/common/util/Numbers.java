package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.exception.InvalidNumberException;

/**
 * Static utility methods for checking certain conditions before operation on numbers.
 */
public final class Numbers {
	
	/**
	 * Checks that the given number is a positive number and throws an {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static float requirePositive(float number) throws InvalidNumberException {
		return requirePositive(number, null);
	}
	
	/**
	 * Checks that the given number is a positive number and throws a customized {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param message The message to be used in the event that an {@link InvalidNumberException} is thrown
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static float requirePositive(float number, String message) throws InvalidNumberException {
		if (number < 0) {
			throw new InvalidNumberException(message != null ? message : "number must be positive");
		} else {
			return number;
		}
	}
	
	/**
	 * Checks that the given number is a positive number and throws an {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static int requirePositive(int number) throws InvalidNumberException {
		return requirePositive(number, null);
	}
	
	/**
	 * Checks that the given number is a positive number and throws a customized {@link InvalidNumberException} if it is not.
	 * @param number The number to check
	 * @param message The message to be used in the event that an {@link InvalidNumberException} is thrown
	 * @return <code>number</code> if it is positive.
	 * @throws InvalidNumberException If <code>number</code> is not positive.
	 */
	public static int requirePositive(int number, String message) throws InvalidNumberException {
		if (number < 0) {
			throw new InvalidNumberException(message != null ? message : "number must be positive");
		} else {
			return number;
		}
	}
	
}
