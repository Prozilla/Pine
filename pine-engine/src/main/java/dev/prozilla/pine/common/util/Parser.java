package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.exception.ParsingException;
import dev.prozilla.pine.common.property.ParsedProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for a stateful parser.
 * @param <T> The type of the result.
 */
public abstract class Parser<T> implements Printable {
	
	private T result;
	private String errorMessage;
	
	public static final String GENERIC_ERROR = "Failed to parse input";
	public static final String UNEXPECTED_END_OF_INPUT_ERROR = "Unexpected end of input";
	
	/**
	 * Parses an input string and throws an exception if the parsing fails.
	 * @param input The input string to parse
	 * @return The parsed value
	 * @throws ParsingException If this parser failed to parse the input string.
	 */
	public T read(String input) throws ParsingException {
		if (!parse(input)) {
			throw new ParsingException(getError());
		}
		return getResult();
	}
	
	/**
	 * Returns the result or {@code null}, if the parsing failed.
	 * @return The result
	 */
	public T getResult() {
		return result;
	}
	
	/**
	 * Returns the error message or {@code null}, if the parsing succeeded.
	 * @return The error message
	 */
	public String getError() {
		return errorMessage;
	}
	
	/**
	 * Parses a string.
	 * @param input The input string to parse
	 * @return {@code true} if the parsing succeeded.
	 */
	public abstract boolean parse(String input);
	
	@Contract("null -> fail; _ -> true")
	protected boolean succeed(T result) {
		this.result = Checks.isNotNull(result, "result");
		errorMessage = null;
		return true;
	}
	
	@Contract("-> false")
	protected boolean fail() {
		return fail(GENERIC_ERROR);
	}
	
	@Contract("null -> fail; _ -> false")
	protected boolean fail(String errorMessage) {
		this.errorMessage = Checks.isNotNull(errorMessage, "errorMessage");
		result = null;
		return false;
	}
	
	@Contract("_ -> new")
	public ParsedProperty<T> parseProperty(VariableProperty<String> inputProperty) {
		return new ParsedProperty<>(inputProperty, this);
	}
	
	@Override
	public @NotNull String toString() {
		StringBuilder stringBuilder = new StringBuilder("Parser: ");
		
		if (result != null) {
			stringBuilder.append("Result: ");
			stringBuilder.append(result);
		} else if (errorMessage != null) {
			stringBuilder.append("Error: ");
			stringBuilder.append(errorMessage);
		} else {
			stringBuilder.append("No info");
		}
		
		return stringBuilder.toString();
	}
	
}
