package dev.prozilla.pine.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Utility methods related to lists.
 *
 * <p>Some methods only work for lists, others work for all instances of a super class of {@link List}, like {@link Collection} or even {@link Iterable}.
 * They are grouped together here for convenience.</p>
 */
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
	public static <E, T extends E> T getInstance(Collection<E> list, Class<T> type) {
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
	
	/**
	 * Returns the first element in a list, or {@code null} if the list is empty.
	 * @param list The list
	 * @return The first element or {@code null}.
	 * @param <E> The type of elements in the list
	 */
	public static <E> E getFirst(List<E> list) {
		if (list.isEmpty()) {
			return null;
		}
		return list.getFirst();
	}
	
	/**
	 * Finds the first element in a list that matches a predicate.
	 * @param list The list
	 * @param predicate The predicate the element must match
	 * @return The first element that matches the predicate, or {@code null} if there was none.
	 * @param <E> The type of elements in the list
	 */
	public static <E> E find(Iterable<E> list, Predicate<E> predicate) {
		for (E element : list) {
			if (predicate.test(element)) {
				return element;
			}
		}
		return null;
	}
	
}
