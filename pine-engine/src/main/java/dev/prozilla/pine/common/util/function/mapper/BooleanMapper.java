package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface BooleanMapper extends FromBooleanMapper<Boolean>, ToBooleanMapper<Boolean> {
	
	@Override
	default Boolean map(Boolean in) {
		return mapToBoolean(in);
	}
	
	@Override
	default Boolean map(boolean in) {
		return mapToBoolean(in);
	}
	
	@Override
	default boolean mapToBoolean(Boolean in) {
		return mapToBoolean(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a boolean.
	 * @param in The original boolean
	 * @return The mapped boolean.
	 */
	boolean mapToBoolean(boolean in);
	
	static BooleanMapper negator() {
		return (input) -> !input;
	}
	
}
