package dev.prozilla.pine.common.string;

public class StringUtils {
	
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
	
}
