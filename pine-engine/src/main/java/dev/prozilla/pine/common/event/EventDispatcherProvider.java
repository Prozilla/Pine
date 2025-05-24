package dev.prozilla.pine.common.event;

public interface EventDispatcherProvider<EventType extends Enum<EventType>, E> extends EventDispatcherContext<EventType, E> {
	
	EventDispatcher<EventType, E> getEventDispatcher();
	
	default void addListener(EventType eventType, EventListener<E> listener) {
		getEventDispatcher().addListener(eventType, listener);
	}
	
	default void removeListener(EventType eventType, EventListener<E> listener) {
		getEventDispatcher().addListener(eventType, listener);
	}
	
	default void invoke(EventType eventType, E event) {
		getEventDispatcher().invoke(eventType, event);
	}
	
}
