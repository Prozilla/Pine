package dev.prozilla.pine.common.util.checks;

import dev.prozilla.pine.common.exception.InvalidNumberException;

import java.util.Objects;

/**
 * Utility class for performing checks on integers.
 */
public final class IntChecks {
	
	private final int value;
	private String name;
	
	public IntChecks(int value) {
		this(value, null);
	}
	
	public IntChecks(int value, String name) {
		this.value = value;
		this.name = Objects.requireNonNullElse(name, "integer");
	}
	
	public IntChecks named(String name) {
		this.name = Objects.requireNonNullElse(name, "integer");
		return this;
	}
	
	public int get() {
		return value;
	}
	
	/**
	 * Checks that the integer is positive.
	 * @param strict Whether to forbid {@code 0}
	 */
	public IntChecks isPositive(boolean strict) throws InvalidNumberException {
		Checks.isPositive(value, strict, String.format("%s must be%s positive", name, strict ? " strictly" : ""));
		return this;
	}
	
	/**
	 * Checks that the integer is greater than another integer.
	 * @param other The other integer
	 * @throws InvalidNumberException If the integer is not greater than the other integer.
	 */
	public IntChecks isGreaterThan(int other) throws InvalidNumberException {
		if (value <= other) {
			throw new InvalidNumberException(name + " must be greater than " + other);
		}
		return this;
	}
	
	/**
	 * Checks that the integer is less than another integer.
	 * @param other The other integer
	 * @throws InvalidNumberException If the integer is not less than the other integer.
	 */
	public IntChecks isLessThan(int other) throws InvalidNumberException {
		if (value >= other) {
			throw new InvalidNumberException(name + " must be less than " + other);
		}
		return this;
	}
	
	/**
	 * Checks that the integer is in the given range.
	 * @param min Lower bound (inclusive)
	 * @param max Upper bound (inclusive)
	 */
	public IntChecks isInRange(int min, int max) throws InvalidNumberException {
		Checks.isInRange(value, min, max, String.format("%s must be between %s and %s", name, min, max));
		return this;
	}
	
}
