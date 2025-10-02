package dev.prozilla.pine.common;

import dev.prozilla.pine.common.lifecycle.Destructible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface CollectionContext<T> extends Iterable<T>, Destructible {
	
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
	
	boolean add(T item);
	
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
	
	default boolean filter(Predicate<T> predicate) {
		List<T> itemsToRemove = new ArrayList<>();
		for (T item : this) {
			if (predicate.test(item)) {
				itemsToRemove.add(item);
			}
		}
		return removeAll(itemsToRemove);
	}
	
	boolean remove(T item);
	
	@Override
	default void destroy() {
		clear();
	}
	
	void clear();
	
	int size();
	
}
