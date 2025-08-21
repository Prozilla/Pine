package dev.prozilla.pine.common.util;

import org.jetbrains.annotations.Contract;

import java.util.function.Function;

public class ObjectUtils {
	
	@Contract("null, _ -> null")
	public static <I, O> O preserveNull(I input, Function<I, O> function) {
		if (input == null) {
			return null;
		}
		return function.apply(input);
	}
	
}
