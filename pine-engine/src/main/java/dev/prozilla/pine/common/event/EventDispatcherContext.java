package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.ContextOf;

import java.util.Objects;

@ContextOf(EventDispatcher.class)
public interface EventDispatcherContext<EventType extends Enum<EventType>, Target, E extends Event<EventType, ? super Target>> {
	
	/**
	 * Equivalent of {@link #addListener(Enum, EventListener)}.
	 */
	default EventListener<E> on(EventType eventType, EventListener<E> listener) {
		return addListener(eventType, listener);
	}
	
	/**
	 * Equivalent of {@link #removeListener(Enum, EventListener)}.
	 */
	default void off(EventType eventType, EventListener<E> listener) {
		removeListener(eventType, listener);
	}
	
	/**
	 * Equivalent of {@link #addListener(Enum, EventListener, boolean)}, where the last argument is {@code true}.
	 */
	default EventListener<E> once(EventType eventType, EventListener<E> listener) {
		return addListener(eventType, listener, true);
	}
	
	/**
	 * Adds a listener that only listens to events of a given type with the given target.
	 * @param eventType The type of event to listen to
	 * @param target The target of the event
	 * @param listener The listener that handles events with the given target
	 * @return The targeted listener that was added.
	 */
	default EventListener<E> addTargetedListener(EventType eventType, Target target, EventListener<E> listener) {
		return addListener(eventType, (event) -> {
			if (Objects.equals(event.getTarget(), target)) {
				listener.handle(event);
			}
		});
	}
	
	/**
	 * Adds a listener that listens to a given type of event.
	 *
	 * <p>Unicity is not required. If a listener is added multiple times, it will be called multiple times per event.</p>
	 * @param eventType The type of event to listen to
	 * @param listener The listener to add
	 * @param once When set to {@code true}, the event listener will be removed after the first event of the given type
	 * @return The listener that was added.
	 */
	default EventListener<E> addListener(EventType eventType, EventListener<E> listener, boolean once) {
		if (once) {
			return addListener(eventType, new EphemeralEventListener<>(this, eventType, listener));
		} else {
			return addListener(eventType, listener);
		}
	}
	
	/**
	 * Adds a listener that listens to a given type of event.
	 *
	 * <p>Unicity is not required. If a listener is added multiple times, it will be called multiple times per event.</p>
	 * @param eventType The type of event to listen to
	 * @param listener The listener to add
	 * @return The listener that was added.
	 */
	EventListener<E> addListener(EventType eventType, EventListener<E> listener);
	
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
	
	class EphemeralEventListener<EventType extends Enum<EventType>, E extends Event<EventType, ?>> implements EventListener<E> {
		
		private final EventDispatcherContext<EventType, ?, E> eventDispatcherContext;
		private final EventType eventType;
		private final EventListener<E> listener;
		
		public EphemeralEventListener(EventDispatcherContext<EventType, ?, E> eventDispatcherContext, EventType eventType, EventListener<E> listener) {
			this.eventDispatcherContext = eventDispatcherContext;
			this.eventType = eventType;
			this.listener = listener;
		}
		
		@Override
		public void handle(E event) {
			listener.handle(event);
			eventDispatcherContext.removeListener(eventType, this);
		}
		
	}
	
}
