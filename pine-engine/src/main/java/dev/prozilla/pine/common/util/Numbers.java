package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.exception.InvalidNumberException;

/**
 * Static utility methods for checking certain conditions before operation on numbers.
 * @deprecated Replaced by {@link Checks} as of 1.2.0
 */
@Deprecated
public final class Numbers {
	
	public static float requirePositive(float number, boolean strict) throws InvalidNumberException {
		return requirePositive(number, strict, null);
	}
	
	public static float requirePositive(float number, boolean strict, String message) throws InvalidNumberException {
		return Checks.isPositive(number, strict, message);
	}
	
	public static int requirePositive(int number, boolean strict) throws InvalidNumberException {
		return requirePositive(number, strict, null);
	}
	
	public static int requirePositive(int number, boolean strict, String message) throws InvalidNumberException {
		return Checks.isPositive(number, strict, message);
	}
	
}
