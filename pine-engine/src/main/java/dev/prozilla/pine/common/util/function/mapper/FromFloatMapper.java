package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface FromFloatMapper<O> extends Mapper<Float, O> {
	
	@Override
	default O map(Float in) {
		return map(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a float to an object.
	 * @param in The original float
	 * @return The mapped object.
	 */
	O map(float in);
	
}
