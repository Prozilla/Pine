package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an object that can dispatch events.
 * @param <E> The type of event this object can dispatch
 */
public abstract class EventDispatcher<EventType extends Enum<EventType>, Target, E extends Event<EventType, ? super Target>> implements EventDispatcherContext<EventType, Target, E>, Destructible {
	
	private final Map<EventType, List<EventListener<E>>> listeners;
	
	protected Logger logger;
	
	public EventDispatcher() {
		listeners = new HashMap<>();
		logger = null;
	}
	
	@Override
	public void addListener(EventType eventType, EventListener<E> listener) {
		Checks.isNotNull(eventType, "eventType");
		Checks.isNotNull(listener, "listener");
		
		if (!listeners.containsKey(eventType)) {
			listeners.put(eventType, new ArrayList<>());
		}
		
		listeners.get(eventType).add(listener);
	}
	
	@Override
	public void removeListener(EventType eventType, EventListener<E> listener) {
		Checks.isNotNull(eventType, "eventType");
		Checks.isNotNull(listener, "listener");
		
		List<EventListener<E>> eventListeners = listeners.get(eventType);
		
		if (eventListeners == null) {
			return;
		}
		
		eventListeners.remove(listener);
	}
	
	@Override
	public void invoke(EventType eventType, Target target) {
		invoke(createEvent(eventType, target));
	}
	
	/**
	 * Creates an event of a given type with a given target.
	 * @param eventType The type of event to create
	 * @param target The target of the event
	 * @return The new event
	 */
	protected abstract E createEvent(EventType eventType, Target target);
	
	/**
	 * Invokes an event.
	 * @param event The event to invoke
	 */
	protected void invoke(E event) {
		List<EventListener<E>> eventListeners = listeners.get(event.getType());
		
		if (eventListeners == null) {
			return;
		}
		
		for (EventListener<E> listener : eventListeners) {
			try {
				listener.handle(event);
			} catch (Exception e) {
				if (logger != null) {
					logger.error("Event listener failed", e);
				} else {
					e.printStackTrace();
				}
			}
			
			if (event.isImmediatePropagationStopped()) {
				break;
			}
		}
		
		if (!event.isPropagationStopped()) {
			propagate(event);
		}
	}
	
	/**
	 * Propagates the event to other objects in relation to the target object.
	 * This method is called after the event has been invoked, unless the propagation was stopped.
	 *
	 * <p>The default implementation does nothing.</p>
	 * @param event The event to propagate
	 */
	protected void propagate(E event) {
	
	}
	
	/**
	 * Sets the logger of this event dispatcher, which is used to log errors thrown by listeners.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Removes all listeners.
	 */
	@Override
	public void destroy() {
		listeners.clear();
	}
	
}
