package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.Cloneable;

import java.util.Objects;

/**
 * Represents an event which takes place on a target.
 *
 * <p>Based on <a href="https://developer.mozilla.org/en-US/docs/Web/API/Event">Event - Web APIs | MDN</a></p>
 * @param <EventType> The types of events this event can represent
 * @param <Target> The type of target this event has
 * @see EventDispatcher
 */
public class Event<EventType extends Enum<EventType>, Target> implements Cloneable<Event<EventType, Target>> {

	protected final EventType type;
	protected final Target target;
	
	protected boolean immediatePropagationStopped;
	protected boolean propagationStopped;
	
	/**
	 * Creates an event of a given type with a given target.
	 * @param type The type of event
	 * @param target The target of the event
	 */
	public Event(EventType type, Target target) {
		this.type = type;
		this.target = target;
		
		immediatePropagationStopped = false;
		propagationStopped = false;
	}
	
	public EventType getType() {
		return type;
	}
	
	public Target getTarget() {
		return target;
	}
	
	/**
	 * Prevents other listeners of this event from being called.
	 */
	public void stopImmediatePropagation() {
		immediatePropagationStopped = true;
		stopPropagation();
	}
	
	/**
	 * Prevents further propagation of this event.
	 */
	public void stopPropagation() {
		propagationStopped = true;
	}
	
	public boolean isImmediatePropagationStopped() {
		return immediatePropagationStopped;
	}
	
	public boolean isPropagationStopped() {
		return propagationStopped;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Event<?, ?> otherEvent && equals(otherEvent);
	}
	
	@Override
	public boolean equals(Event<EventType, Target> other) {
		return other.getType() == type && Objects.equals(other.getTarget(), target);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(type, target);
	}
	
	@Override
	public Event<EventType, Target> clone() {
		return new Event<>(type, target);
	}
	
}
