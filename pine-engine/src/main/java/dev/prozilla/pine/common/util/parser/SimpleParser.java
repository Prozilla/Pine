package dev.prozilla.pine.common.util.parser;

import dev.prozilla.pine.common.util.Parser;
import org.jetbrains.annotations.Contract;

/**
 * A simple parser that uses a parsing function.
 */
public class SimpleParser<T> extends Parser<T> {
	
	/** The function that parses the input */
	private final ParseFunction<T> parseFunction;
	
	/**
	 * Creates a simple parser using a parsing function.
	 * @param parseFunction The parsing function to use
	 */
	public SimpleParser(ParseFunction<T> parseFunction) {
		this.parseFunction = parseFunction;
	}
	
	@Override
	public boolean parse(String input) {
		T result = parseFunction.parse(input);
		if (result == null) {
			return fail();
		} else {
			return succeed(result);
		}
	}
	
	/**
	 * Creates a simple parser using {@link ParseFunction#parseInt(String)}.
	 */
	@Contract("-> new")
	public static SimpleParser<Integer> intParser() {
		return new SimpleParser<>(ParseFunction::parseInt);
	}
	
	/**
	 * Creates a simple parser using {@link ParseFunction#parseFloat(String)}.
	 */
	@Contract("-> new")
	public static SimpleParser<Float> floatParser() {
		return new SimpleParser<>(ParseFunction::parseFloat);
	}
	
	/**
	 * Creates a simple parser using {@link ParseFunction#parseBoolean(String)}.
	 */
	@Contract("-> new")
	public static SimpleParser<Boolean> booleanParser() {
		return new SimpleParser<>(ParseFunction::parseBoolean);
	}
	
}
