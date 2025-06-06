package dev.prozilla.pine.common.event;

public class SimpleEventDispatcher<EventType extends Enum<EventType>, Target> extends EventDispatcher<EventType, Target, Event<EventType, Target>> {
	
	@Override
	protected Event<EventType, Target> createEvent(EventType eventType, Target target) {
		return new Event<>(eventType, target);
	}
	
}
