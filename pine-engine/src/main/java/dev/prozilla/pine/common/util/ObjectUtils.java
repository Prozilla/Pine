package dev.prozilla.pine.common.util;

import org.jetbrains.annotations.Contract;

import java.util.function.Function;

/**
 * Utility methods related to objects.
 */
public final class ObjectUtils {
	
	private ObjectUtils() {}
	
	/**
	 * Applies a function to an object and returns the result, or immediately returns {@code null} without calling the function if the input object is {@code null}.
	 * @param input The input object
	 * @param function The function to apply to the object
	 * @return The result of the function, or {@code null}.
	 * @param <I> The type of the input object
	 * @param <O> The type of the result object
	 */
	@Contract("null, _ -> null")
	public static <I, O> O preserveNull(I input, Function<I, O> function) {
		if (input == null) {
			return null;
		}
		return function.apply(input);
	}
	
	/**
	 * Safely unboxes a boxed value and turns {@code null} into the default value.
	 * @param value The boxed value
	 * @return The unboxed value.
	 */
	public static int unbox(Integer value) {
		return value != null ? value : 0;
	}
	
	/**
	 * Safely unboxes a boxed value and turns {@code null} into the default value.
	 * @param value The boxed value
	 * @return The unboxed value.
	 */
	public static float unbox(Float value) {
		return value != null ? value : 0;
	}
	
	/**
	 * Safely unboxes a boxed value and turns {@code null} into the default value.
	 * @param value The boxed value
	 * @return The unboxed value.
	 */
	public static boolean unbox(Boolean value) {
		return value != null ? value : false;
	}
	
	/**
	 * Safely unboxes a boxed value and turns {@code null} into the default value.
	 * @param value The boxed value
	 * @return The unboxed value.
	 */
	public static char unbox(Character value) {
		return value != null ? value : '\u0000';
	}
	
}
