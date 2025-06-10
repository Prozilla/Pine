package dev.prozilla.pine.common.event;

/**
 * Represents an event which takes place on a target.
 *
 * <p>Based on <a href="https://developer.mozilla.org/en-US/docs/Web/API/Event">Event - Web APIs | MDN</a></p>
 * @param <EventType> The types of events this event can represent
 * @param <Target> The type of target this event has
 * @see EventDispatcher
 */
public class Event<EventType extends Enum<EventType>, Target> {

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
}
