package dev.prozilla.pine.common.util.function;

/**
 * Represents a container that supports applying a function to its value while preserving its structure.
 * @param <T> The type of value
 */
@FunctionalInterface
public interface Functor<T> {
	
	/**
	 * Applies a function to the value of this functor.
	 * @param mapper The function to apply
	 * @return The functor containing the mapped value
	 * @param <S> The output type of the mapper
	 */
	<S> Functor<S> map(Mapper<T, S> mapper);
	
}
