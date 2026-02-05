package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface FloatToBooleanMapper extends FromFloatMapper<Boolean>, ToBooleanMapper<Float> {
	
	@Override
	default Boolean map(Float in) {
		return mapToBoolean(in);
	}
	
	@Override
	default Boolean map(float in) {
		return mapToBoolean(in);
	}
	
	@Override
	default boolean mapToBoolean(Float in) {
		return mapToBoolean(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a float to a boolean.
	 * @param in The original float
	 * @return The mapped boolean.
	 */
	boolean mapToBoolean(float in);
	
	@Override
	default FloatToBooleanMapper negate() {
		return then(BooleanMapper.negator());
	}
	
	/**
	 * Creates a mapper that maps: {@code float} &rarr; {@code boolean} &rarr; {@code boolean}.
	 * @see #then(Mapper)
	 */
	@Override
	default FloatToBooleanMapper then(BooleanMapper mapper) {
		return (input) -> mapper.mapToBoolean(mapToBoolean(input));
	}
	
}
