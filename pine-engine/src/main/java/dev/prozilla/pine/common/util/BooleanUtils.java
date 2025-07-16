package dev.prozilla.pine.common.util;

public final class BooleanUtils {
	
	private BooleanUtils() {}
	
	public static boolean isTrue(String input) {
		return "true".equalsIgnoreCase(input);
	}
	
	public static boolean isFalse(String input) {
		return "false".equalsIgnoreCase(input);
	}
	
	public static int toInt(boolean value) {
		return value ? 1 : 0;
	}
	
}
