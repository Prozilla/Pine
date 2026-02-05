package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface FloatMapper extends FromFloatMapper<Float>, ToFloatMapper<Float> {
	
	@Override
	default Float map(Float in) {
		return mapToFloat(in);
	}
	
	@Override
	default Float map(float in) {
		return mapToFloat(in);
	}
	
	@Override
	default float mapToFloat(Float in) {
		return mapToFloat(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a float.
	 * @param in The original float
	 * @return The mapped float.
	 */
	float mapToFloat(float in);
	
}

