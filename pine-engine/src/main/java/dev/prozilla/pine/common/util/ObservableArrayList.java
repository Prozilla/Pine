package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.event.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ObservableArrayList<E> extends ArrayList<E> implements ObservableList<E> {

	private final EventDispatcher<E> dispatcher = new EventDispatcher<>();
	
	@Override
	public boolean add(E element) {
		boolean added = super.add(element);
		if (added) {
			invoke(EventType.ELEMENT_ADD);
		}
		return added;
	}
	
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		invoke(EventType.ELEMENT_ADD);
	}
	
	@Override
	public E remove(int index) {
		E element = super.remove(index);
		invoke(EventType.ELEMENT_REMOVE);
		return element;
	}
	
	@Override
	public E removeFirst() {
		E element = super.removeFirst();
		invoke(EventType.ELEMENT_REMOVE);
		return element;
	}
	
	@Override
	public E removeLast() {
		E element = super.removeLast();
		invoke(EventType.ELEMENT_REMOVE);
		return element;
	}
	
	@Override
	public boolean remove(Object object) {
		boolean removed =  super.remove(object);
		if (removed) {
			invoke(EventType.ELEMENT_REMOVE);
		}
		return removed;
	}
	
	@Override
	public boolean addAll(@NotNull Collection<? extends E> collection) {
		boolean added = super.addAll(collection);
		if (added) {
			invoke(collection.size() == 1 ? EventType.ELEMENT_ADD : EventType.ELEMENTS_ADD);
		}
		return added;
	}
	
	@Override
	public boolean addAll(int index, @NotNull Collection<? extends E> collection) {
		boolean added = super.addAll(index, collection);
		if (added) {
			invoke(collection.size() == 1 ? EventType.ELEMENT_ADD : EventType.ELEMENTS_ADD);
		}
		return added;
	}
	
	@Override
	public boolean removeAll(@NotNull Collection<?> collection) {
		boolean removed =  super.removeAll(collection);
		if (removed) {
			invoke(collection.size() == 1 ? EventType.ELEMENT_ADD : EventType.ELEMENTS_ADD);
		}
		return removed;
	}
	
	@Override
	public boolean retainAll(@NotNull Collection<?> collection) {
		boolean removed = super.retainAll(collection);
		if (removed) {
			invoke(EventType.ELEMENTS_REMOVE);
		}
		return removed;
	}
	
	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		if (fromIndex == toIndex) {
			invoke(EventType.ELEMENT_REMOVE);
		} else {
			invoke(EventType.ELEMENTS_REMOVE);
		}
	}
	
	@Override
	public boolean removeIf(@NotNull Predicate<? super E> filter) {
		boolean removed =  super.removeIf(filter);
		if (removed) {
			invoke(EventType.ELEMENTS_REMOVE);
		}
		return removed;
	}
	
	@Override
	public void replaceAll(@NotNull UnaryOperator<E> operator) {
		super.replaceAll(operator);
		invoke(EventType.ELEMENTS_REPLACE);
	}
	
	@Override
	public void sort(Comparator<? super E> c) {
		super.sort(c);
		invoke(EventType.PERMUTATE);
	}
	
	@Override
	public EventListener<Event<E>> addListener(EventType eventType, EventListener<Event<E>> listener) {
		return dispatcher.addListener(eventType, listener);
	}
	
	@Override
	public void removeListener(EventType eventType, EventListener<Event<E>> listener) {
		dispatcher.removeListener(eventType, listener);
	}
	
	public boolean invoke(EventType eventType) {
		return invoke(eventType, this);
	}
	
	@Override
	public boolean invoke(EventType eventType, List<E> target) {
		boolean invoked = dispatcher.invoke(eventType, target);
		if (eventType != EventType.UPDATE) {
			invoke(EventType.UPDATE, target);
		}
		return invoked;
	}
	
	private static class EventDispatcher<E> extends dev.prozilla.pine.common.event.EventDispatcher<EventType, List<E>, Event<E>> {
		@Override
		protected Event<E> createEvent(EventType eventType, List<E> target) {
			return new Event<>(eventType, target);
		}
	}
}
