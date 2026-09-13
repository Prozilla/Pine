package dev.prozilla.pine.common.util;

import org.jetbrains.annotations.Contract;

public final class StringUtils {
	
	private StringUtils() {}
	
	/**
	 * Returns the string representation of an object, or {@code null} if the object is {@code null}.
	 * @see Object#toString()
	 */
	@Contract("null -> null; !null -> !null")
	public static String toString(Object object) {
		return ObjectUtils.preserveNull(object, Object::toString);
	}
	
	/**
	 * Converts the first letter of a given string to upper case.
	 */
	public static String capitalize(String string) {
		return lengthOf(string) > 1 ? string.substring(0, 1).toUpperCase() + string.substring(1) : toUpperCase(string);
	}
	
	/**
	 * @see String#toUpperCase()
	 */
	@Contract("null -> null; !null -> !null")
	public static String toUpperCase(String string) {
		return ObjectUtils.preserveNull(string, String::toUpperCase);
	}
	
	/**
	 * @see String#toLowerCase()
	 */
	@Contract("null -> null; !null -> !null")
	public static String toLowerCase(String string) {
		return ObjectUtils.preserveNull(string, String::toLowerCase);
	}
	
	/**
	 * @see String#trim()
	 */
	@Contract("null -> null; !null -> !null")
	public static String trim(String string) {
		return ObjectUtils.preserveNull(string, String::trim);
	}
	
	/**
	 * @see String#length()
	 */
	public static int lengthOf(String string) {
		return string == null ? 0 : string.length();
	}
	
	/**
	 * @see String#isEmpty()
	 */
	@Contract("null -> true")
	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
	/**
	 * @see String#isBlank()
	 */
	@Contract("null -> true")
	public static boolean isBlank(String string) {
		return string == null || string.isBlank();
	}
	
	public static int findClosingParenthesis(String input, int openIndex) {
		int depth = 0;
		for (int i = openIndex; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '(') {
				depth++;
			} else if (c == ')') {
				depth--;
				if (depth == 0) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static boolean containsOnce(String string, String query) {
		int index = string.indexOf(query);
		return index >= 0 && index == string.lastIndexOf(query);
	}
	
}
