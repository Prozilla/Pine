package dev.prozilla.pine.common.array;

import dev.prozilla.pine.core.component.Component;

public final class ArrayUtils {
	
	/**
	 * Checks if two arrays have any overlapping elements.
	 * @param arrayA First array
	 * @param arrayB Second array
	 * @return True if any of the elements in <code>arrayA</code> also appear in <code>arrayB</code>.
	 * @param <O> Type of the array elements
	 */
	public static <O> boolean overlaps(O[] arrayA, O[] arrayB) {
		if (arrayA == null || arrayB == null || arrayA.length == 0 || arrayB.length == 0) {
			return false;
		}
		
		for (O elementA : arrayA) {
			for (O elementB : arrayB) {
				if (elementA.equals(elementB)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
