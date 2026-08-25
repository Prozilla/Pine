package dev.prozilla.pine.extensions.pinet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A wrapper around {@link List} to construct pipelines.
 * @param <E> The type of elements in this pipeline
 */
public class Pipeline<E> implements Iterable<E> {
	
	private final List<E> elements;
	
	public Pipeline() {
		elements = new ArrayList<>();
	}
	
	@SafeVarargs
	public Pipeline(E... elements) {
		this();
		add(elements);
	}
	
	public Pipeline(Collection<? extends  E> elements) {
		this.elements = new ArrayList<>(elements);
	}
	
	public Pipeline<E> append(Pipeline<? extends E> pipeline) {
		for (E element : pipeline) {
			add(element);
		}
		return this;
	}
	
	@SafeVarargs
	public final Pipeline<E> add(E... elements) {
		for (E element : elements) {
			add(element);
		}
		return this;
	}
	
	public Pipeline<E> add(E element) {
		elements.add(element);
		return this;
	}
	
	public Pipeline<E> addAll(Collection<? extends  E> elements) {
		this.elements.addAll(elements);
		return this;
	}
	
	public Pipeline<E> clear() {
		elements.clear();
		return this;
	}
	
	public int size() {
		return elements.size();
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}
	
	public List<E> toList() {
		return elements;
	}
	
}
