package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.Transmittable;
import dev.prozilla.pine.common.exception.InvalidObjectException;
import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.common.util.checks.Checks;

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
	 * Creates a mapper that maps: {@link I} &rarr; {@link O} &rarr; {@code boolean}.
	 * @see #then(Mapper)
	 */
	default ToBooleanMapper<I> then(ToBooleanMapper<O> mapper) {
		return (input) -> mapper.mapToBoolean(map(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@link O} &rarr; {@code float}.
	 * @see #then(Mapper)
	 */
	default ToFloatMapper<I> then(ToFloatMapper<O> mapper) {
		return (input) -> mapper.mapToFloat(map(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@link O} &rarr; {@code int}.
	 * @see #then(Mapper)
	 */
	default ToIntMapper<I> then(ToIntMapper<O> mapper) {
		return (input) -> mapper.mapToInt(map(input));
	}
	
	/**
	 * Creates a mapper that chains this mapper and another mapper: {@link I} &rarr; {@link O} &rarr; {@link T}.
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
	 * @throws InvalidObjectException If {@code mapper} is {@code null}.
	 * @see ObjectUtils#preserveNull(Object, Function)
	 */
	static <T, S> Mapper<T, S> preserveNull(Mapper<T, S> mapper) throws InvalidObjectException {
		Checks.isNotNull(mapper, "mapper");
		return (input) -> ObjectUtils.preserveNull(input, mapper::map);
	}
	
	/**
	 * Creates a mapper that replaces {@code null} values.
	 * @param replacement The value to replace {@code null} with.
	 * @return A new mapper that replaces {@code null} with {@code replacement}.
	 * @param <T> The type of object to map
	 * @throws InvalidObjectException If {@code replacement} is {@code null}.
	 */
	static <T> Mapper<T, T> replaceNull(T replacement) throws InvalidObjectException {
		Checks.isNotNull(replacement, "replacement");
		return (input) -> input == null ? replacement : input;
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
	 * @throws InvalidObjectException If {@code supplier} is {@code null}.
	 */
	static <T extends Transmittable<S>, S> Mapper<T, S> transmitTo(Supplier<S> supplier) throws InvalidObjectException {
		Checks.isNotNull(supplier, "supplier");
		return (source) -> Transmittable.transmitBetween(source, supplier.get());
	}

}
