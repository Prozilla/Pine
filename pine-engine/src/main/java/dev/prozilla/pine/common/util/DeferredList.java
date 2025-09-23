package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.lifecycle.Destructible;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implementation of an array list which defers modifications of elements until the current iteration is complete, to prevent concurrent modification exceptions.
 * @param <E> The type of elements in this list
 */
public class DeferredList<E> extends ArrayList<E> implements Destructible {
	
	private final List<E> elementsToAdd = new ArrayList<>();
	private final List<Object> elementsToRemove = new ArrayList<>();
	private int currentIterations = 0;
	
	@Override
	public boolean add(E element) {
		if (!isIterating()) {
			return super.add(element);
		}
		
		elementsToAdd.add(element);
		elementsToRemove.remove(element);
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		if (!isIterating()) {
			return super.addAll(collection);
		}
		
		elementsToAdd.addAll(collection);
		elementsToRemove.removeAll(collection);
		return true;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) throws ConcurrentModificationException {
		requireNotIterating();
		return super.addAll(index, collection);
	}
	
	@Override
	public void add(int index, E element) throws ConcurrentModificationException {
		requireNotIterating();
		super.add(index, element);
	}
	
	@Override
	public boolean remove(Object object) {
		if (!isIterating()) {
			return super.remove(object);
		}
		
		elementsToRemove.add(object);
		elementsToAdd.remove(object);
		return true;
	}
	
	@Override
	public boolean removeAll(Collection<?> collection) {
		if (!isIterating()) {
			return super.removeAll(collection);
		}
		
		elementsToRemove.addAll(collection);
		elementsToAdd.removeAll(collection);
		return true;
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter) throws ConcurrentModificationException {
		requireNotIterating();
		return super.removeIf(filter);
	}
	
	@Override
	public E remove(int index) throws ConcurrentModificationException {
		requireNotIterating();
		return super.remove(index);
	}
	
	@Override
	public void clear() {
		if (!isIterating()) {
			super.clear();
		}
		
		elementsToAdd.clear();
		elementsToRemove.addAll(this);
	}
	
	/**
	 * Returns the size of the snapshot of this list.
	 * @return The size of the snapshot.
	 */
	public int snapshotSize() {
		return getSnapshot().size();
	}
	
	/**
	 * Checks if the snapshot of this list is empty.
	 * @return {@code true} if the snapshot is empty.
	 */
	public boolean isSnapshotEmpty() {
		return getSnapshot().isEmpty();
	}
	
	/**
	 * Returns a snapshot of what this list will look like after the current iterations are done and the deferred operations are executed.
	 *
	 * <p>If there are no current iterations, this will simply return {@code this}.</p>
	 * @return A snapshot of this list after executing the deferred operations.
	 */
	public List<E> getSnapshot() {
		if (!isIterating() || (elementsToAdd.isEmpty() && elementsToRemove.isEmpty())) {
			return this;
		}
		
		List<E> elements = new ArrayList<>(this);
		elements.addAll(elementsToAdd);
		elements.removeAll(elementsToRemove);
		return elements;
	}
	
	@Override
	public @NotNull Iterator<E> iterator() {
		Iterator<E> iterator = super.iterator();
		startIteration();
		return new Iterator<>() {
			private boolean finished = false;
			
			@Override
			public boolean hasNext() {
				boolean hasNext = iterator.hasNext();
				if (!hasNext && !finished) {
					finished = true;
					endIteration();
				}
				return hasNext;
			}
			
			@Override
			public E next() {
				return iterator.next();
			}
		};
	}
	
	@Override
	public void forEach(Consumer<? super E> action) {
		startIteration();
		super.forEach(action);
		endIteration();
	}
	
	@Override
	public void sort(Comparator<? super E> c) {
		startIteration();
		super.sort(c);
		endIteration();
	}
	
	/**
	 * Marks the start of a new iteration of this list.
	 */
	public void startIteration() {
		currentIterations++;
	}
	
	/**
	 * Marks the end of an ongoing iteration of this list.
	 */
	public void endIteration() {
		if (!isIterating()) {
			throw new IllegalStateException("List is not being iterated");
		}
		
		currentIterations--;
		
		if (!isIterating()) {
			addAll(elementsToAdd);
			removeAll(elementsToRemove);
			
			elementsToAdd.clear();
			elementsToRemove.clear();
		}
	}
	
	private void requireNotIterating() throws ConcurrentModificationException {
		if (isIterating()) {
			throw new ConcurrentModificationException("Operation is not permitted while iterating");
		}
	}
	
	/**
	 * Checks if this list is being iterated.
	 * @return {@code true} if this list is being iterated.
	 */
	public boolean isIterating() {
		return currentIterations > 0;
	}
	
	/**
	 * Removes all elements from this list.
	 */
	@Override
	public void destroy() {
		requireNotIterating();
		super.clear();
		elementsToAdd.clear();
		elementsToRemove.clear();
	}
	
}
