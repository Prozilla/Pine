package dev.prozilla.pine.common.util.function.predicate;

import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Represents a predicate of a boolean.
 */
@FunctionalInterface
public interface FloatPredicate extends Predicate<Float> {
	
	@Override
	default boolean test(Float input) {
		return test((float)input);
	}
	
	/**
	 * Evaluates this predicate on the given float.
	 * @param input The input value
	 * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
	 */
	boolean test(float input);
	
	default @NotNull FloatPredicate and(FloatPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) && other.test(value);
	}
	
	default @NotNull FloatPredicate or(FloatPredicate other) {
		Checks.isNotNull(other, "other");
		return (value) -> test(value) || other.test(value);
	}
	
	@Override
	default @NotNull FloatPredicate negate() {
		return (value) -> !test(value);
	}
	
}
