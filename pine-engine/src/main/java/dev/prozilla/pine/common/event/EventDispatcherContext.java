package dev.prozilla.pine.common.event;

public interface EventDispatcherContext<Event extends Enum<Event>> {
	
	/**
	 * Adds a listener that listens to a given type of event.
	 * @param eventType The type of event to listen to
	 * @param listener The listener to add
	 */
	void addListener(Event eventType, EventListener listener);
	
	/**
	 * Removes a listener that listened to a given type of event.
	 * @param eventType The type of event that was being listened to
	 * @param listener The listener to remove
	 */
	void removeListener(Event eventType, EventListener listener);
	
	/**
	 * Invokes the event of a given type.
	 * @param eventType The type of event to invoke
	 */
	void invoke(Event eventType);
	
}
