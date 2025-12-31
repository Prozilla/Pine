package dev.prozilla.pine.common;

import dev.prozilla.pine.common.lifecycle.Destructible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface CollectionContext<T> extends Iterable<T>, Destructible {
	
	/**
	 * Adds all given items to this collection.
	 * @param items The items to add.
	 * @return {@code true} if the collection was modified.
	 */
	default boolean addAll(T... items) {
		if (items == null) {
			return false;
		}
		boolean modified = false;
		for (T item : items) {
			if (add(item)) {
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * Adds all items from a given collection to this collection.
	 * @param items The items to add.
	 * @return {@code true} if the collection was modified.
	 */
	default boolean addAll(Collection<T> items) {
		if (items == null) {
			return false;
		}
		boolean modified = false;
		for (T item : items) {
			if (add(item)) {
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * Adds an item to this collection.
	 * @param item The item to add
	 * @return {@code true} if the collection was modified.
	 */
	boolean add(T item);
	
	/**
	 * Removes all given items from this collection.
	 * @param items The items to remove.
	 * @return {@code true} if the collection was modified.
	 */
	default boolean removeAll(T... items) {
		if (items == null) {
			return false;
		}
		boolean modified = false;
		for (T item : items) {
			if (remove(item)) {
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * Removes all items from this collection that match the given predicate.
	 * @param predicate The predicate to evaluate on each item
	 * @return {@code true} if the collection was modified.
	 */
	default boolean filter(Predicate<T> predicate) {
		List<T> itemsToRemove = new ArrayList<>();
		for (T item : this) {
			if (predicate.test(item)) {
				itemsToRemove.add(item);
			}
		}
		return removeAll(itemsToRemove);
	}
	
	/**
	 * Removes all items from a given collection from this collection.
	 * @param items The items to remove.
	 * @return {@code true} if the collection was modified.
	 */
	default boolean removeAll(Collection<T> items) {
		if (items == null) {
			return false;
		}
		boolean modified = false;
		for (T item : items) {
			if (remove(item)) {
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * Removes an item from this collection.
	 * @param item The item to remove
	 * @return {@code true} if the collection was modified.
	 */
	boolean remove(T item);
	
	/**
	 * Destroys this collection by removing all items.
	 */
	@Override
	default void destroy() {
		clear();
	}
	
	/**
	 * Removes all items from this collection.
	 */
	void clear();
	
	/**
	 * Returns the size of this collection.
	 * @return The size of this collection.
	 */
	int size();
	
}
