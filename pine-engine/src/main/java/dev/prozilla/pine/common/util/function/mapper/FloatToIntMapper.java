package dev.prozilla.pine.common.util.function.mapper;

import dev.prozilla.pine.common.util.ObjectUtils;

@FunctionalInterface
public interface FloatToIntMapper extends FromFloatMapper<Integer>, ToIntMapper<Float> {
	
	@Override
	default Integer map(Float in) {
		return mapToInt(in);
	}
	
	@Override
	default Integer map(float in) {
		return mapToInt(in);
	}
	
	@Override
	default int mapToInt(Float in) {
		return mapToInt(ObjectUtils.unbox(in));
	}
	
	/**
	 * Maps a float to an integer.
	 * @param in The original float
	 * @return The mapped integer.
	 */
	int mapToInt(float in);
	
	/**
	 * Creates a mapper that maps: {@code float} &rarr; {@code boolean} &rarr; {@code boolean}.
	 * @see #then(Mapper)
	 */
	@Override
	default FloatToIntMapper then(IntMapper mapper) {
		return (input) -> mapper.mapToInt(mapToInt(input));
	}
	
}
