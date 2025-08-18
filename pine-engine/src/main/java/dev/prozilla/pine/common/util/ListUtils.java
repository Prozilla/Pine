package dev.prozilla.pine.common.util;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {
	
	private ListUtils() {}
	
	public static <E> List<E> createSingleton(E element) {
		List<E> list = new ArrayList<>();
		list.add(element);
		return list;
	}
	
	/**
	 * Finds the first element in a list of a given type.
	 *
	 * <p>This method is not suitable for frequent usage or usage with large lists.</p>
	 * @param list The list to search in
	 * @param type The type to search for
	 * @return The element of the given type, or {@code null} if there is none.
	 * @param <E> The type of elements in the list
	 * @param <T> The type of element to search for
	 */
	public static <E, T extends E> T getInstance(List<E> list, Class<T> type) {
		if (list.isEmpty()) {
			return null;
		}
		
		for (E element : list) {
			if (type.isInstance(element)) {
				return type.cast(element);
			}
		}
		
		return null;
	}
	
}
