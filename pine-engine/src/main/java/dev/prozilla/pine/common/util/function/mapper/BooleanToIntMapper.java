package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface BooleanToIntMapper extends FromBooleanMapper<Integer>, ToIntMapper<Boolean> {
	
	@Override
	default Integer map(Boolean in) {
		return mapToInt(in);
	}
	
	@Override
	default Integer map(boolean in) {
		return mapToInt(in);
	}
	
	@Override
	default int mapToInt(Boolean in) {
		return mapToInt(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a boolean to an integer.
	 * @param in The original boolean
	 * @return The mapped integer.
	 */
	int mapToInt(boolean in);
	
	/**
	 * Creates a mapper that maps: {@code boolean} &rarr; {@code int} &rarr; {@code int}.
	 * @see #then(Mapper)
	 */
	@Override
	default BooleanToIntMapper then(IntMapper mapper) {
		return (input) -> mapper.mapToInt(mapToInt(input));
	}
	
}
