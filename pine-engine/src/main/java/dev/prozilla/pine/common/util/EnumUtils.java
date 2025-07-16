package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.IntEnum;

public final class EnumUtils {
	
	private EnumUtils() {}
	
	public static <E extends IntEnum> boolean hasIntValue(E[] values, int value) {
		for (E enumValue : values) {
			if (enumValue.getValue() == value) {
				return true;
			}
		}
		return false;
	}
	
}
