package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface IntMapper extends FromIntMapper<Integer>, ToIntMapper<Integer> {
	
	@Override
	default Integer map(Integer in) {
		return mapToInt(in);
	}
	
	@Override
	default Integer map(int in) {
		return mapToInt(in);
	}
	
	@Override
	default int mapToInt(Integer in) {
		return mapToInt(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps an integer.
	 * @param in The original integer
	 * @return The mapped integer.
	 */
	int mapToInt(int in);
	
}
