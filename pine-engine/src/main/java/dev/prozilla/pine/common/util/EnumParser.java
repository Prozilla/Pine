package dev.prozilla.pine.common.util;

public class EnumParser<E extends Enum<E>> extends Parser<E> {
	
	private final E[] values;
	
	public EnumParser(E[] values) {
		this.values = values;
	}
	
	@Override
	public boolean parse(String input) {
		E value = ArrayUtils.findByString(values, input);
		
		if (value == null) {
			return fail("Invalid enum value");
		}
		
		return succeed(value);
	}
	
}
