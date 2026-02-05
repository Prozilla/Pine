package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface FromBooleanMapper<O> extends Mapper<Boolean, O> {
	
	@Override
	default O map(Boolean in) {
		return map(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a boolean to an object.
	 * @param in The original boolean
	 * @return The mapped object.
	 */
	O map(boolean in);
	
}
