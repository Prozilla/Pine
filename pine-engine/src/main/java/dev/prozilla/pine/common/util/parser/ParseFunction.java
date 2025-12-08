package dev.prozilla.pine.common.util.parser;

import dev.prozilla.pine.common.util.BooleanUtils;
import dev.prozilla.pine.common.util.function.mapper.Mapper;

/**
 * A function that parses an input string.
 * @param <T> The type of result
 */
@FunctionalInterface
public interface ParseFunction<T> extends Mapper<String, T> {
	
	/**
	 * Parses an input string.
	 * @param input The input string to parse
	 * @return The parsed value, or {@code null} if the parsing failed.
	 */
	T parse(String input);
	
	@Override
	default T map(String in) {
		return parse(in);
	}
	
	static Integer parseInt(String input) {
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	static Float parseFloat(String input) {
		try {
			return Float.valueOf(input);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	static Boolean parseBoolean(String input) {
		if (BooleanUtils.isTrue(input)) {
			return Boolean.TRUE;
		} else if (BooleanUtils.isFalse(input)) {
			return Boolean.FALSE;
		} else {
			return null;
		}
	}
	
}
