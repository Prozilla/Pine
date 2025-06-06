package dev.prozilla.pine.common.event;

/**
 * An event listener handles events when they occur.
 * @param <E> The type of event to listen to
 */
@FunctionalInterface
public interface EventListener<E extends Event<?, ?>> {
	
	/**
	 * Handles an event.
	 * @param event The event to handle
	 */
	void handle(E event);
	
}
