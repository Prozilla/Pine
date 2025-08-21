package dev.prozilla.pine.common.util;

import org.jetbrains.annotations.Contract;

public final class BooleanUtils {
	
	private BooleanUtils() {}
	
	@Contract("null -> true")
	public static boolean isNotTrue(Boolean bool) {
		return !isTrue(bool);
	}
	
	@Contract("null -> true")
	public static boolean isNotFalse(Boolean bool) {
		return !isFalse(bool);
	}
	
	@Contract("null -> false")
	public static boolean isTrue(Boolean bool) {
		return Boolean.TRUE.equals(bool);
	}
	
	@Contract("null -> false")
	public static boolean isFalse(Boolean bool) {
		return Boolean.FALSE.equals(bool);
	}
	
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
