package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface IntToFloatMapper extends FromIntMapper<Float>, ToFloatMapper<Integer> {
	
	@Override
	default Float map(Integer in) {
		return mapToFloat(in);
	}
	
	@Override
	default Float map(int in) {
		return mapToFloat(in);
	}
	
	@Override
	default float mapToFloat(Integer in) {
		return mapToFloat(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps an integer to a float.
	 * @param in The original integer
	 * @return The mapped float.
	 */
	float mapToFloat(int in);
	
	/**
	 * Creates a mapper that maps: {@code int} &rarr; {@code float} &rarr; {@code float}.
	 * @see #then(Mapper)
	 */
	@Override
	default IntToFloatMapper then(FloatMapper mapper) {
		return (input) -> mapper.mapToFloat(mapToFloat(input));
	}
	
}
