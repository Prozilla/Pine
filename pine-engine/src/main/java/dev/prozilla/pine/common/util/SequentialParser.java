package dev.prozilla.pine.common.util;

import org.jetbrains.annotations.Contract;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class SequentialParser<T> extends Parser<T> {
	
	private String input;
	private int cursor;
	
	protected T intermediate;
	
	/**
	 * Represents a predicate of one character.
	 */
	@FunctionalInterface
	public interface CharPredicate extends Predicate<Character> {
		
		default boolean test(Character character) {
			return test((char)character);
		}
		
		/**
		 * Evaluates this predicate on the given character.
		 * @param character The input character
		 * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
		 */
		boolean test(char character);
		
	}
	
	@Contract("-> true")
	protected boolean succeed() {
		return succeed(intermediate);
	}
	
	protected void startStep(String input, T intermediate) {
		setInput(input);
		this.intermediate = intermediate;
	}
	
	/**
	 * Recursively parses a new input string, then restores the previous state of this parser.
	 * @param input The input string to parse
	 * @return {@code true} if the recursive parsing was successful.
	 */
	protected boolean parseRecursively(String input) {
		return recursiveStep(() -> parse(input));
	}
	
	/**
	 * Executes a function and restores the previous state of this parser.
	 * @param callback The function to call
	 * @return The return value of the function.
	 * @param <R> The return type of the function
	 */
	protected <R> R recursiveStep(Supplier<R> callback) {
		String previousInput = input;
		int previousCursor = cursor;
		T previousIntermediate = intermediate;
		
		R result = callback.get();
		
		setInput(previousInput);
		setCursor(previousCursor);
		intermediate = previousIntermediate;
		
		return result;
	}
	
	/**
	 * Sets the input string and moves the cursor to the first character.
	 * @param input The input string
	 */
	protected void setInput(String input) {
		resetCursor();
		this.input = input.trim();
	}
	
	/**
	 * Returns the input string.
	 * @return The input string.
	 */
	protected String getInput() {
		return input;
	}
	
	protected boolean isMethodCall(String methodName) {
		int oldCursor = cursor;
		skipWhitespace();
		boolean isMethodCall = readWhile((character) -> !Character.isWhitespace(character) && character != '(').equalsIgnoreCase(methodName);
		setCursor(oldCursor);
		return isMethodCall;
	}
	
	protected String readBetweenParentheses() {
		return readBetweenCharacters('(', ')');
	}
	
	protected String readBetweenSquareBrackets() {
		return readBetweenCharacters('[', ']');
	}
	
	protected String readBetweenCharacters(char before, char after) {
		skipUntilChar(before);
		moveCursor();
		return readWhile((character) -> character != after);
	}
	
	/**
	 * Returns the sequence of characters constructed by validating the predicate and moving the cursor by one each time it is true.
	 * @param predicate The predicate
	 * @return The string of characters.
	 */
	protected String readWhile(CharPredicate predicate) {
		int start = cursor;
		while (!endOfInput() && predicate.test(getChar())) {
			moveCursor();
		}
		if (start >= cursor) {
			return "";
		}
		return getInput().substring(start, cursor);
	}
	
	/**
	 * Moves the cursor to the next character that is not whitespace.
	 */
	protected void skipWhitespace() {
		skipUntil((character) -> !Character.isWhitespace(character));
	}
	
	/**
	 * Moves the cursor until it points to one of the given characters.
	 * @param characters The characters to look for
	 */
	protected void skipUntilAnyChar(char... characters) {
		skipUntil((currentChar) -> {
			for (char character : characters) {
				if (currentChar == character) {
					return true;
				}
			}
			return false;
		});
	}
	
	/**
	 * Moves the cursor until it points to the given character.
	 * @param character The character to look for
	 */
	protected void skipUntilChar(char character) {
		while (!endOfInput() && isNotChar(character)) {
			moveCursor();
		}
	}
	
	/**
	 * Moves the cursor until the predicate evaluates to {@code true}.
	 * @param predicate The predicate
	 */
	protected void skipUntil(CharPredicate predicate) {
		while (!endOfInput() && !predicate.test(getChar())) {
			moveCursor();
		}
	}
	
	/**
	 * Moves the cursor by one if the current character is equal to the given character.
	 * @param character The character to check for
	 */
	protected void skipIfChar(char character) {
		if (isChar(character)) {
			moveCursor();
		}
	}
	
	/**
	 * Moves the cursor by one of the predicate evaluates to {@code true} for the current character.
	 * @param predicate The predicate
	 */
	protected void skipIf(CharPredicate predicate) {
		if (!endOfInput() && predicate.test(getChar())) {
			moveCursor();
		}
	}
	
	/**
	 * Checks if the cursor points to a character that is not equal to the given character.
	 * @param character The character to check for
	 * @return {@code} true if the cursor points to a character that is not equal to {@code character}.
	 */
	protected boolean isNotChar(char character) {
		return !endOfInput() && getChar() != character;
	}
	
	/**
	 * Checks if the cursor points to a character that is equal to the given character.
	 * @param character The character to check for
	 * @return {@code} true if the cursor points to a character that is equal to {@code character}.
	 */
	protected boolean isChar(char character) {
		return !endOfInput() && getChar() == character;
	}
	
	/**
	 * Returns the character the cursor is pointing to.
	 * @return The character the cursor is pointing to.
	 */
	protected char getChar() {
		return input.charAt(cursor);
	}
	
	/**
	 * Moves the cursor to the start.
	 */
	protected void resetCursor() {
		cursor = 0;
	}
	
	/**
	 * Moves the cursor by one.
	 */
	protected void moveCursor() {
		moveCursor(1);
	}
	
	/**
	 * Moves the cursor by a given amount.
	 * @param amount The amount to move
	 */
	protected void moveCursor(int amount) {
		cursor += amount;
	}
	
	/**
	 * Returns the position of the cursor.
	 * @return The position of the cursor.
	 */
	protected final int getCursor() {
		return cursor;
	}
	
	/**
	 * Moves the cursor to a given position
	 * @param cursor The position to move the cursor to
	 */
	public void setCursor(int cursor) {
		this.cursor = cursor;
	}
	
	/**
	 * Checks if the cursor is at the end of the input and therefore not pointing to any character.
	 * @return {@code true} if the cursor is at the end of the input.
	 */
	protected boolean endOfInput() {
		return cursor >= getCharCount();
	}
	
	/**
	 * Returns the amount of characters in the input string.
	 * @return The amount of characters in the input string.
	 */
	protected int getCharCount() {
		return input.length();
	}
	
}
