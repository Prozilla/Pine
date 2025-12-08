package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.Transmittable;
import dev.prozilla.pine.common.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A function that maps one type to another type.
 * @param <I> The input type
 * @param <O> The output type
 */
@FunctionalInterface
public interface Mapper<I, O>  {
	
	/**
	 * Maps an object.
	 * @param in The original object
	 * @return The mapped object.
	 */
	O map(I in);
	
	/**
	 * Creates a mapper that chains this mapper and another mapper.
	 * @param mapper The mapper to chain with this mapper
	 * @return A mapper that chains both mappers.
	 * @param <T> The output type of the resulting mapper
	 */
	default <T> Mapper<I, T> then(Mapper<O, T> mapper) {
		return (input) -> mapper.map(map(input));
	}
	
	/**
	 * Creates a mapper that preserves {@code null} values.
	 * @param mapper The mapper to use for non-null values
	 * @return A new mapper that preserves {@code null} values.
	 * @param <T> The input type of the mapper
	 * @param <S> The output type of the mapper
	 */
	@SuppressWarnings("unchecked")
	static <T,S> Mapper<T, S> preserveNull(Mapper<T, S> mapper) {
		return (input) -> ObjectUtils.preserveNull(input, (Function<T,S>)mapper);
	}
	
	/**
	 * Creates a mapper that converts objects to strings.
	 * @return A mapper that converts objects to strings.
	 * @param <T> The input type
	 * @see Objects#toString(Object)
	 */
	static <T> Mapper<T, String> mapToString() {
		return Objects::toString;
	}
	
	/**
	 * Creates a mapper that transmits data between two objects.
	 * @param supplier The supplier of the target object
	 * @return A new mapper that transmits data.
	 * @param <T> The source object type
	 * @param <S> The target object type
	 */
	static <T extends Transmittable<S>, S> Mapper<T, S> transmitTo(Supplier<S> supplier) {
		return (source) -> Transmittable.transmitBetween(source, supplier.get());
	}

}
