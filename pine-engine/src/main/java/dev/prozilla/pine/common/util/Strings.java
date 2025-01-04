package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.exception.InvalidStringException;
import org.gradle.internal.impldep.javax.annotation.Nonnull;

/**
 * Static utility methods for checking certain conditions before operation on strings.
 */
public class Strings {
	
	/**
	 * Checks that the given string is not blank and throws an {@link InvalidStringException} if it is not.
	 * @param string The string to check
	 * @return <code>string</code> if it is not blank.
	 * @throws InvalidStringException If <code>string</code> is blank.
	 */
	public static String requireNonBlank(@Nonnull String string) throws InvalidStringException {
		return requireNonBlank(string, null);
	}
	
	/**
	 * Checks that the given string is not blank and throws a customized {@link InvalidStringException} if it is.
	 * @param string The string to check
	 * @param message The message to be used in the event that an {@link InvalidStringException} is thrown
	 * @return <code>string</code> if it is not blank.
	 * @throws InvalidStringException If <code>string</code> is blank.
	 */
	public static String requireNonBlank(@Nonnull String string, String message) throws InvalidStringException {
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
	public static String requireNonEmpty(@Nonnull String string) throws InvalidStringException {
		return requireNonEmpty(string, null);
	}
	
	/**
	 * Checks that the given string is not empty and throws a customized {@link InvalidStringException} if it is.
	 * @param string The string to check
	 * @param message The message to be used in the event that an {@link InvalidStringException} is thrown
	 * @return <code>string</code> if it is not empty.
	 * @throws InvalidStringException If <code>string</code> is empty.
	 */
	public static String requireNonEmpty(@Nonnull String string, String message) throws InvalidStringException {
		if (string.isEmpty()) {
			throw new InvalidStringException(message != null ? message : "string must not be empty");
		} else {
			return string;
		}
	}
	
}
