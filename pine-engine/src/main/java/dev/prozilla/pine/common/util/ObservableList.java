package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.event.EventDispatcherContext;
import dev.prozilla.pine.common.event.EventListener;

import java.util.List;

public interface ObservableList<E> extends List<E>, EventDispatcherContext<ObservableList.EventType, List<E>, ObservableList.Event<E>> {
	
	default void onUpdate(EventListener<Event<E>> callback) {
		addListener(EventType.UPDATE, callback);
	}
	
	default void onAdd(EventListener<Event<E>> callback) {
		addListener(EventType.ELEMENT_ADD, callback);
		addListener(EventType.ELEMENTS_ADD, callback);
	}
	
	default void onRemove(EventListener<Event<E>> callback) {
		addListener(EventType.ELEMENT_REMOVE, callback);
		addListener(EventType.ELEMENTS_REMOVE, callback);
	}
	
	enum EventType {
		/** Invoked when any element in the list has changed. */
		UPDATE,
		/** Invoked when an element is added to the list. */
		ELEMENT_ADD,
		/** Invoked when an element is removed from the list. */
		ELEMENT_REMOVE,
		/** Invoked when an element is replaced. */
		ELEMENT_REPLACE,
		/** Invoked when multiple elements are added to the list. */
		ELEMENTS_ADD,
		/** Invoked when multiple elements are removed from the list. */
		ELEMENTS_REMOVE,
		/** Invoked when multiple elements are replaced. */
		ELEMENTS_REPLACE,
		/** Invoked when the list is permutated. */
		PERMUTATE
	}
	
	class Event<E> extends dev.prozilla.pine.common.event.Event<ObservableList.EventType, List<E>> {
		
		public Event(EventType eventType, List<E> target) {
			super(eventType, target);
		}
	}

}
