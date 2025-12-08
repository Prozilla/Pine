package dev.prozilla.pine.common.util.function.mapper;

public interface IntMapper extends Mapper<Integer, Integer> {
	
	@Override
	default Integer map(Integer in) {
		return map((int)in);
	}
	
	/**
	 * Maps an integer.
	 * @param in The original integer
	 * @return The mapped integer.
	 */
	int map(int in);
	
	default IntMapper then(IntMapper mapper) {
		return (input) -> mapper.map(map(input));
	}
	
}
