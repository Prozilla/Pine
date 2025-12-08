package dev.prozilla.pine.common.util.function.predicate;

import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Represents a predicate of one character.
 */
@FunctionalInterface
public interface CharPredicate extends Predicate<Character> {
	
	default boolean test(Character input) {
		return test((char)input);
	}
	
	/**
	 * Evaluates this predicate on the given character.
	 * @param input The input value
	 * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
	 */
	boolean test(char input);
	
	default @NotNull CharPredicate and(CharPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) && other.test(value);
	}
	
	default @NotNull CharPredicate or(CharPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) || other.test(value);
	}
	
	@Override
	default @NotNull CharPredicate negate() {
		return (value) -> !test(value);
	}
	
}
