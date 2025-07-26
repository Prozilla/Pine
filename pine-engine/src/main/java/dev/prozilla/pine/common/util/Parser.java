package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.Printable;

import java.util.Objects;

/**
 * Abstract class for a stateful parser.
 * @param <T> The type of the result.
 */
public abstract class Parser<T> implements Printable {
	
	private T result;
	private String errorMessage;
	
	/**
	 * Parses an input string and throws an exception if the parsing fails.
	 * @param input The input string to parse
	 * @return The parsed value or {@code null}, if the parsing failed.
	 * @throws IllegalArgumentException If this parser failed to parse the input string.
	 */
	public T read(String input) throws IllegalArgumentException {
		if (!parse(input)) {
			throw new IllegalArgumentException(getError());
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
	
	protected boolean succeed(T result) {
		this.result = Objects.requireNonNull(result, "result must not be null");
		errorMessage = null;
		return true;
	}
	
	protected boolean fail() {
		return fail("Failed to parse input");
	}
	
	protected boolean fail(String errorMessage) {
		this.errorMessage = Objects.requireNonNull(errorMessage, "errorMessage must not be null");
		result = null;
		return false;
	}
	
	@Override
	public String toString() {
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
