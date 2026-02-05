package dev.prozilla.pine.common.util.function.mapper;

@FunctionalInterface
public interface ToFloatMapper<I> extends Mapper<I, Float> {
	
	@Override
	default Float map(I in) {
		return mapToFloat(in);
	}
	
	/**
	 * Maps an object to a float.
	 * @param in The original object
	 * @return The mapped float.
	 */
	float mapToFloat(I in);
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code float} &rarr; {@link T}.
	 * @see #then(Mapper)
	 */
	default <T> Mapper<I, T> then(FromFloatMapper<T> mapper) {
		return (input) -> mapper.map(mapToFloat(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code float} &rarr; {@code float}.
	 * @see #then(Mapper)
	 */
	default ToFloatMapper<I> then(FloatMapper mapper) {
		return (input) -> mapper.mapToFloat(mapToFloat(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code float} &rarr; {@code boolean}.
	 * @see #then(Mapper)
	 */
	default ToBooleanMapper<I> then(FloatToBooleanMapper mapper) {
		return (input) -> mapper.mapToBoolean(mapToFloat(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code float} &rarr; {@code int}.
	 * @see #then(Mapper)
	 */
	default ToIntMapper<I> then(FloatToIntMapper mapper) {
		return (input) -> mapper.mapToInt(mapToFloat(input));
	}
	
}
