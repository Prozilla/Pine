package dev.prozilla.pine.common.event;

public interface EventDispatcherProvider<EventType extends Enum<EventType>, Target, E extends Event<EventType, Target>> extends EventDispatcherContext<EventType, Target, E> {
	
	EventDispatcher<EventType, Target, E> getEventDispatcher();
	
	default void addListener(EventType eventType, EventListener<E> listener) {
		getEventDispatcher().addListener(eventType, listener);
	}
	
	default void removeListener(EventType eventType, EventListener<E> listener) {
		getEventDispatcher().addListener(eventType, listener);
	}
	
	default void invoke(EventType eventType, Target target) {
		getEventDispatcher().invoke(eventType, target);
	}
	
}
