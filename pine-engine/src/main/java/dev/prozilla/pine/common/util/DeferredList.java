package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.lifecycle.Destructible;
import org.gradle.tooling.model.UnsupportedMethodException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implementation of array list which defers the adding and removing of elements until the current iteration is complete, to prevent concurrent modification errors.
 * @param <E>
 */
public class DeferredList<E> extends ArrayList<E> implements Destructible {
	
	private final List<E> elementsToAdd = new ArrayList<>();
	private final List<E> elementsToRemove = new ArrayList<>();
	private boolean isIterating = false;
	
	@Override
	public boolean add(E element) {
		if (!isIterating) {
			return super.add(element);
		}
		
		elementsToAdd.add(element);
		elementsToRemove.remove(element);
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		if (!isIterating) {
			return super.addAll(collection);
		}
		
		elementsToAdd.addAll(collection);
		elementsToRemove.removeAll(collection);
		return true;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedMethodException("Adding an element at an index is not supported");
	}
	
	@Override
	public void add(int index, E element) throws UnsupportedMethodException {
		throw new UnsupportedMethodException("Adding an element at an index is not supported");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object object) {
		if (!isIterating) {
			return super.remove(object);
		}
		
		elementsToRemove.add((E)object);
		elementsToAdd.remove(object);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> collection) {
		if (!isIterating) {
			return super.removeAll(collection);
		}
		
		elementsToRemove.addAll((Collection<? extends E>)collection);
		elementsToAdd.removeAll(collection);
		return true;
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		throw new UnsupportedMethodException("Adding an element at an index is not supported");
	}
	
	@Override
	public E remove(int index) throws UnsupportedMethodException {
		throw new UnsupportedMethodException("Adding an element at an index is not supported");
	}
	
	@Override
	public void clear() {
		if (!isIterating) {
			super.clear();
		}
		
		elementsToAdd.clear();
		elementsToRemove.addAll(this);
	}
	
	@Override
	public boolean contains(Object o) {
		return super.contains(o) || elementsToAdd.contains(o);
	}
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && elementsToAdd.isEmpty();
	}
	
	@Override
	public Iterator<E> iterator() {
		startIteration();
		return super.iterator();
	}
	
	@Override
	public void forEach(Consumer<? super E> action) {
		boolean alreadyIterating = isIterating;
		if (!alreadyIterating) {
			startIteration();
		}
		super.forEach(action);
		if (!alreadyIterating) {
			endIteration();
		}
	}
	
	@Override
	public void sort(Comparator<? super E> c) {
		boolean alreadyIterating = isIterating;
		if (!alreadyIterating) {
			startIteration();
		}
		super.sort(c);
		if (!alreadyIterating) {
			endIteration();
		}
	}
	
	public void startIteration() {
		isIterating = true;
	}
	
	public void endIteration() {
		if (!isIterating) {
			return;
		}
		
		isIterating = false;
		
		addAll(elementsToAdd);
		removeAll(elementsToRemove);
		
		elementsToAdd.clear();
		elementsToRemove.clear();
	}
	
	public boolean isIterating() {
		return isIterating;
	}
	
	@Override
	public void destroy() {
		if (isIterating) {
			throw new ConcurrentModificationException("DeferredList can not be destroyed while iterating");
		}
		
		super.clear();
		elementsToAdd.clear();
		elementsToRemove.clear();
	}
	
}
