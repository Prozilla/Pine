package dev.prozilla.pine.common.event;

public interface EventDispatcherContext<EventType extends Enum<EventType>, Target, E extends Event<EventType, ? super Target>> {
	
	/**
	 * Adds a listener that listens to a given type of event.
	 * @param eventType The type of event to listen to
	 * @param listener The listener to add
	 */
	void addListener(EventType eventType, EventListener<E> listener);
	
	/**
	 * Removes a listener that listened to a given type of event.
	 * @param eventType The type of event that was being listened to
	 * @param listener The listener to remove
	 */
	void removeListener(EventType eventType, EventListener<E> listener);
	
	/**
	 * Invokes the event of a given type.
	 * @param eventType The type of event to invoke
	 * @param target The target of the event
	 */
	void invoke(EventType eventType, Target target);
	
}
