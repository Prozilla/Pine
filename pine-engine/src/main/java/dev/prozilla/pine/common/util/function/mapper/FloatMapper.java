package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

public interface FloatMapper extends Mapper<Float, Float> {
	
	@Override
	default Float map(Float in) {
		return map(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a float.
	 * @param in The original float
	 * @return The mapped float.
	 */
	float map(float in);
	
	default FloatMapper then(FloatMapper mapper) {
		return (input) -> mapper.map(map(input));
	}
	
}
