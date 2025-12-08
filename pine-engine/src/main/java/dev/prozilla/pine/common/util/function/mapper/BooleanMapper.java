package dev.prozilla.pine.common.util.function.mapper;

public interface BooleanMapper extends Mapper<Boolean, Boolean> {
	
	@Override
	default Boolean map(Boolean in) {
		return map((boolean)in);
	}
	
	/**
	 * Maps a boolean.
	 * @param in The original boolean
	 * @return The mapped boolean.
	 */
	boolean map(boolean in);
	
	default BooleanMapper then(BooleanMapper mapper) {
		return (input) -> mapper.map(map(input));
	}
	
}
