package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface BooleanToFloatMapper extends FromBooleanMapper<Float>, ToFloatMapper<Boolean> {
	
	@Override
	default Float map(Boolean in) {
		return mapToFloat(in);
	}
	
	@Override
	default Float map(boolean in) {
		return mapToFloat(in);
	}
	
	@Override
	default float mapToFloat(Boolean in) {
		return mapToFloat(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a boolean to a float.
	 * @param in The original boolean
	 * @return The mapped float.
	 */
	float mapToFloat(boolean in);
	
	/**
	 * Creates a mapper that maps: {@code boolean} &rarr; {@code float} &rarr; {@code float}.
	 * @see #then(Mapper)
	 */
	@Override
	default BooleanToFloatMapper then(FloatMapper mapper) {
		return (input) -> mapper.mapToFloat(mapToFloat(input));
	}
	
}
