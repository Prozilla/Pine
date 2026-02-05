package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface FromIntMapper<O> extends Mapper<Integer, O> {
	
	@Override
	default O map(Integer in) {
		return map(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps an integer to an object.
	 * @param in The original integer
	 * @return The mapped object.
	 */
	O map(int in);
	
}
