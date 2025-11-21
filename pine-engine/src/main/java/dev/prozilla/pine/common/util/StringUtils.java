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
