package dev.prozilla.pine.common.util.function.mapper;

@FunctionalInterface
public interface ToBooleanMapper<I> extends Mapper<I, Boolean> {
	
	@Override
	default Boolean map(I in) {
		return mapToBoolean(in);
	}
	
	/**
	 * Maps an object to a boolean.
	 * @param in The original object
	 * @return The mapped boolean.
	 */
	boolean mapToBoolean(I in);
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code boolean} &rarr; {@link T}.
	 * @see #then(Mapper) 
	 */
	default <T> Mapper<I, T> then(FromBooleanMapper<T> mapper) {
		return (input) -> mapper.map(mapToBoolean(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code boolean} &rarr; {@code boolean}.
	 * @see #then(Mapper)
	 */
	default ToBooleanMapper<I> then(BooleanMapper mapper) {
		return (input) -> mapper.mapToBoolean(mapToBoolean(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code boolean} &rarr; {@code float}.
	 * @see #then(Mapper)
	 */
	default ToFloatMapper<I> then(BooleanToFloatMapper mapper) {
		return (input) -> mapper.mapToFloat(mapToBoolean(input));
	}
	
	/**
	 * Creates a mapper that maps: {@link I} &rarr; {@code boolean} &rarr; {@code int}.
	 * @see #then(Mapper)
	 */
	default ToIntMapper<I> then(BooleanToIntMapper mapper) {
		return (input) -> mapper.mapToInt(mapToBoolean(input));
	}
	
	default ToBooleanMapper<I> negate() {
		return then(BooleanMapper.negator());
	}
	
}
