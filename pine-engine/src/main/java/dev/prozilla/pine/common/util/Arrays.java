package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.exception.InvalidArrayException;

/**
 * Static utility methods for checking certain conditions before operation on arrays.
 * @deprecated Replaced by {@link Checks} as of 1.2.0
 */
@Deprecated
public final class Arrays {
	
	public static <E> void requireDisjunct(E[] arrayA, E[] arrayB) throws InvalidArrayException {
		requireDisjunct(arrayA, arrayB, null);
	}
	
	public static <E> void requireDisjunct(E[] arrayA, E[] arrayB, String message) throws InvalidArrayException {
		Checks.areDisjunct(arrayA, arrayB, message);
	}
	
	public static <E> E[] requireNonEmpty(E[] array) throws InvalidArrayException {
		return requireNonEmpty(array, null);
	}
	
	public static <E> E[] requireNonEmpty(E[] array, String message) throws InvalidArrayException {
		return Checks.isNotEmpty(array, message);
	}
	
	public static <E> E[] requireMinLength(E[] array, int minLength) throws InvalidArrayException {
		return requireMinLength(array, minLength, null);
	}
	
	public static <E> E[] requireMinLength(E[] array, int minLength, String message) throws InvalidArrayException {
		return Checks.hasMinLength(array, minLength, message);
	}
	
	public static <E> E[] requireMaxLength(E[] array, int maxLength) throws InvalidArrayException {
		return requireMinLength(array, maxLength, null);
	}
	
	public static <E> E[] requireMaxLength(E[] array, int maxLength, String message) throws InvalidArrayException {
		return Checks.hasMaxLength(array, maxLength, message);
	}
	
	public static <E> E[] requireLength(E[] array, int length) throws InvalidArrayException {
		return requireLength(array, length, null);
	}
	
	public static <E> E[] requireLength(E[] array, int length, String message) throws InvalidArrayException {
		return Checks.hasLength(array, length, message);
	}
}
