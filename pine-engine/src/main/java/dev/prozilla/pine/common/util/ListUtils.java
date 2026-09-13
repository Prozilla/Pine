package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.property.selection.WrapMode;
import org.jetbrains.annotations.Contract;

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
		if (isEmpty(list)) {
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
		if (isEmpty(list)) {
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
	
	/**
	 * Gets the element at a given position in the list, treating the list as circular.
	 *
	 * <p>
	 *     E.g., if {@code position} is {@code -1}, the last element will be returned,
	 *     and if {@code position} is {@code list.size()}, the first element will be returned.
	 * </p>
	 * <p>
	 *     If the list is empty or {@code null}, {@code null} will be returned.
	 * </p>
	 * @param list The list
	 * @return {@code true} if the list is empty or {@code null}.
	 * @param <E> The type of elements in the list
	 */
	@Contract("null, _ -> null")
	public static <E> E getCircular(List<E> list, int position) {
		return !isEmpty(list) ? WrapMode.REPEAT.getElement(position, list) : null;
	}
	
	/**
	 * Checks whether the given list is empty or {@code null}.
	 * @param list The list
	 * @return {@code true} if the list is empty or {@code null}.
	 * @param <E> The type of elements in the list
	 */
	@Contract("null -> true")
	public static <E> boolean isEmpty(Collection<E> list) {
		return list == null || list.isEmpty();
	}
	
}
