package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.ProviderOf;

@ProviderOf(EventDispatcher.class)
@FunctionalInterface
public interface EventDispatcherProvider<EventType extends Enum<EventType>, Target, E extends Event<EventType, Target>> extends EventDispatcherContext<EventType, Target, E> {
	
	EventDispatcher<EventType, Target, E> getEventDispatcher();
	
	@Override
	default EventListener<E> addListener(EventType eventType, EventListener<E> listener) {
		return getEventDispatcher().addListener(eventType, listener);
	}
	
	@Override
	default void removeListener(EventType eventType, EventListener<E> listener) {
		getEventDispatcher().addListener(eventType, listener);
	}
	
	@Override
	default void invoke(EventType eventType, Target target) {
		getEventDispatcher().invoke(eventType, target);
	}
	
}
