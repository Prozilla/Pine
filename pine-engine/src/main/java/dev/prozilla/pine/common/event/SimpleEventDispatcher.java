package dev.prozilla.pine.common.event;

/**
 * An event dispatcher that uses the {@link Event} class for events. Events are created using an event type and an optional target.
 *
 * <p>This is useful in situations where creating a subclass of {@link Event} is overkill and the basic functionality of {@link Event} suffices.</p>
 * @param <EventType>
 * @param <Target>
 */
public class SimpleEventDispatcher<EventType extends Enum<EventType>, Target> extends EventDispatcher<EventType, Target, Event<EventType, Target>> {
	
	/**
	 * Invokes an event of a given type without a target.
	 * @param eventType The type of event to invoke
	 */
	public void invoke(EventType eventType) {
		invoke(eventType, null);
	}
	
	@Override
	protected Event<EventType, Target> createEvent(EventType eventType, Target target) {
		return new Event<>(eventType, target);
	}
	
}
