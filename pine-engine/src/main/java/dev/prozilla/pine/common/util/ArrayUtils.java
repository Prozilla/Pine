package dev.prozilla.pine.common.util;

import java.util.Random;

public final class ArrayUtils {
	
	private ArrayUtils() {}
	
	/**
	 * Checks if two arrays have any overlapping elements.
	 * @param arrayA First array
	 * @param arrayB Second array
	 * @return True if any of the elements in <code>arrayA</code> also appear in <code>arrayB</code>.
	 * @param <E> Type of the array elements
	 */
	public static <E> boolean overlaps(E[] arrayA, E[] arrayB) {
		if (arrayA == null || arrayB == null || arrayA.length == 0 || arrayB.length == 0) {
			return false;
		}
		
		for (E elementA : arrayA) {
			if (contains(arrayB, elementA)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if an array contains an element.
	 * @return True if one of the elements of the array is equal to <code>element</code>.
	 * @param <E> Type of the array elements
	 */
	public static <E> boolean contains(E[] array, E element) {
		for (E arrayElement : array) {
			if (arrayElement.equals(element)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Finds the element in an array of which the result of {@link Object#toString()} is equal to a given string.
	 * @param array The array to search in
	 * @param string The string representation of the element to search for
	 * @return The element of which the string representation matches <code>string</code>, or <code>null</code> if there isn't one.
	 * @param <E> Type of the array elements
	 */
	public static <E> E findByString(E[] array, String string) {
		for (E element : array) {
			if (element.toString().equals(string)) {
				return element;
			}
		}
		
		return null;
	}
	
	public static <E> void shuffle(E[] array, long seed) {
		Random rand = new Random(seed);
		for (int i = array.length - 1; i > 0; i--) {
			int j = rand.nextInt(i + 1);
			E temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}
	
	public static void shuffle(double[] array, long seed) {
		Random rand = new Random(seed);
		for (int i = array.length - 1; i > 0; i--) {
			int j = rand.nextInt(i + 1);
			double temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}
	
}
