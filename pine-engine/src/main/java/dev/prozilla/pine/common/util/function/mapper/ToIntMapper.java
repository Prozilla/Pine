package dev.prozilla.pine.common.util.function.mapper;

@FunctionalInterface
public interface ToIntMapper<I> extends Mapper<I, Integer> {
	
	@Override
	default Integer map(I in) {
		return mapToInt(in);
	}
	
	/**
	 * Maps an object to an integer.
	 * @param in The original object
	 * @return The mapped integer.
	 */
	int mapToInt(I in);
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code int} &rarr; {@link T}.
	 * @see #then(Mapper)
	 */
	default <T> Mapper<I, T> then(FromIntMapper<T> mapper) {
		return (input) -> mapper.map(mapToInt(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code int} &rarr; {@code int}.
	 * @see #then(Mapper)
	 */
	default ToIntMapper<I> then(IntMapper mapper) {
		return (input) -> mapper.mapToInt(mapToInt(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code int} &rarr; {@code boolean}.
	 * @see #then(Mapper)
	 */
	default ToBooleanMapper<I> then(IntToBooleanMapper mapper) {
		return (input) -> mapper.mapToBoolean(mapToInt(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code int} &rarr; {@code float}.
	 * @see #then(Mapper)
	 */
	default ToFloatMapper<I> then(IntToFloatMapper mapper) {
		return (input) -> mapper.mapToFloat(mapToInt(input));
	}
	
}
