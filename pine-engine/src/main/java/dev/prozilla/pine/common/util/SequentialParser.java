package dev.prozilla.pine.common.util;

import java.util.function.Predicate;

public abstract class SequentialParser<T> extends Parser<T> {
	
	private String input;
	private int cursor;
	
	protected T intermediate;
	
	@FunctionalInterface
	public interface CharPredicate extends Predicate<Character> {
		
		default boolean test(Character character) {
			return test((char)character);
		}
		
		boolean test(char character);
		
	}
	
	protected boolean succeed() {
		return succeed(intermediate);
	}
	
	protected void startStep(String input, T intermediate) {
		setInput(input);
		this.intermediate = intermediate;
	}
	
	protected void setInput(String input) {
		resetCursor();
		this.input = input.trim();
	}
	
	protected String getInput() {
		return input;
	}
	
	protected void skipWhitespace() {
		skipUntil((character) -> !Character.isWhitespace(character));
	}
	
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
	
	protected void skipUntilChar(char character) {
		while (!endOfInput() && getChar() != character) {
			moveCursor();
		}
	}
	
	protected void skipUntil(CharPredicate predicate) {
		while (!endOfInput() && !predicate.test(getChar())) {
			moveCursor();
		}
	}
	
	protected char getChar() {
		return input.charAt(cursor);
	}
	
	protected void skipIfChar(char character) {
		if (!endOfInput() && getChar() == character) {
			moveCursor();
		}
	}
	
	protected void skipIf(CharPredicate predicate) {
		if (!endOfInput() && predicate.test(getChar())) {
			moveCursor();
		}
	}
	
	protected void resetCursor() {
		cursor = 0;
	}
	
	protected void moveCursor() {
		moveCursor(1);
	}
	
	protected void moveCursor(int amount) {
		cursor += amount;
	}
	
	protected int getCursor() {
		return cursor;
	}
	
	protected boolean endOfInput() {
		return cursor >= getCharCount();
	}
	
	protected int getCharCount() {
		return input.length();
	}
	
}
