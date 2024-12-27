package dev.prozilla.pine.common.array;

public final class ArrayUtils {
	
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
}
