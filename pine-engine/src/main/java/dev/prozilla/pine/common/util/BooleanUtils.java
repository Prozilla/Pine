package dev.prozilla.pine.common.util;

import org.jetbrains.annotations.Contract;

public final class BooleanUtils {
	
	private BooleanUtils() {}
	
	@Contract("null -> false")
	public static boolean isTrue(String input) {
		return "true".equalsIgnoreCase(input);
	}
	
	@Contract("null -> false")
	public static boolean isFalse(String input) {
		return "false".equalsIgnoreCase(input);
	}
	
	public static int toInt(boolean value) {
		return value ? 1 : 0;
	}
	
}
