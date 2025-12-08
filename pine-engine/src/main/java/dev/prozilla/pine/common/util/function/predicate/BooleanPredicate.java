package dev.prozilla.pine.common.util.function.predicate;

import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Represents a predicate of a boolean.
 */
@FunctionalInterface
public interface BooleanPredicate extends Predicate<Boolean> {
	
	@Override
	default boolean test(Boolean input) {
		return test((boolean)input);
	}
	
	/**
	 * Evaluates this predicate on the given boolean.
	 * @param input The input value
	 * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
	 */
	boolean test(boolean input);
	
	default @NotNull BooleanPredicate and(BooleanPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) && other.test(value);
	}
	
	default @NotNull BooleanPredicate or(BooleanPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) || other.test(value);
	}
	
	@Override
	default @NotNull BooleanPredicate negate() {
		return (value) -> !test(value);
	}
	
}
