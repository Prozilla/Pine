package dev.prozilla.pine.common;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public interface CollectionProvider<T> extends CollectionContext<T> {
	
	Collection<T> items();
	
	@Override
	default boolean add(T item) {
		return items().add(item);
	}
	
	@Override
	default boolean remove(T item) {
		return items().remove(item);
	}
	
	@Override
	default void clear() {
		items().clear();
	}
	
	@Override
	default int size() {
		return items().size();
	}
	
	@Override
	default @NotNull Iterator<T> iterator() {
		return items().iterator();
	}
	
}
