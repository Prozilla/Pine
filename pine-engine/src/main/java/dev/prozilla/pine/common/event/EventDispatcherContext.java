package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.ContextOf;

@ContextOf(EventDispatcher.class)
public interface EventDispatcherContext<EventType extends Enum<EventType>, Target, E extends Event<EventType, ? super Target>> {
	
	/**
	 * Adds a listener that listens to a given type of event.
	 *
	 * <p>Unicity is not required. If a listener is added multiple times, it will be called multiple times per event.</p>
	 * @param eventType The type of event to listen to
	 * @param listener The listener to add
	 */
	void addListener(EventType eventType, EventListener<E> listener);
	
	/**
	 * Removes a listener that was listening to a given type of event.
	 * @param eventType The type of event that was being listened to
	 * @param listener The listener to remove
	 */
	void removeListener(EventType eventType, EventListener<E> listener);
	
	/**
	 * Invokes the event of a given type with a given target.
	 * @param eventType The type of event to invoke
	 * @param target The target of the event
	 */
	void invoke(EventType eventType, Target target);
	
}
