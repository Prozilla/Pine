package dev.prozilla.pine.common.util.function.predicate;

import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Represents a predicate of a boolean.
 */
@FunctionalInterface
public interface IntPredicate extends Predicate<Integer>, java.util.function.IntPredicate {
	
	@Override
	default boolean test(Integer input) {
		return test(ObjectUtils.unbox(input));
	}
	
	/**
	 * Evaluates this predicate on the given int.
	 * @param input The input value
	 * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
	 */
	boolean test(int input);
	
	default @NotNull IntPredicate and(IntPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) && other.test(value);
	}
	
	default @NotNull IntPredicate or(IntPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) || other.test(value);
	}
	
	@Override
	default @NotNull IntPredicate negate() {
		return (value) -> !test(value);
	}
	
}
