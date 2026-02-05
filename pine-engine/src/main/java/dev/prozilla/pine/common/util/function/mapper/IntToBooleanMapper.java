package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface IntToBooleanMapper extends FromIntMapper<Boolean>, ToBooleanMapper<Integer> {
	
	@Override
	default Boolean map(Integer in) {
		return mapToBoolean(in);
	}
	
	@Override
	default Boolean map(int in) {
		return mapToBoolean(in);
	}
	
	@Override
	default boolean mapToBoolean(Integer in) {
		return mapToBoolean(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps an integer to a boolean.
	 * @param in The original integer
	 * @return The mapped boolean.
	 */
	boolean mapToBoolean(int in);
	
	@Override
	default IntToBooleanMapper negate() {
		return then(BooleanMapper.negator());
	}
	
	/**
	 * Creates a mapper that maps: {@code int} &rarr; {@code boolean} &rarr; {@code boolean}.
	 * @see #then(Mapper)
	 */
	@Override
	default IntToBooleanMapper then(BooleanMapper mapper) {
		return (input) -> mapper.mapToBoolean(mapToBoolean(input));
	}
	
}
