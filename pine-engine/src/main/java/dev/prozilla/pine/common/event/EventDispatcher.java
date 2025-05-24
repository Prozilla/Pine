package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcher<EventType extends Enum<EventType>, E> implements EventDispatcherContext<EventType, E> {
	
	private final Map<EventType, List<EventListener<E>>> listeners;
	
	protected Logger logger;
	
	public EventDispatcher() {
		listeners = new HashMap<>();
		logger = null;
	}
	
	@Override
	public void addListener(EventType eventType, EventListener<E> listener) {
		if (!listeners.containsKey(eventType)) {
			listeners.put(eventType, new ArrayList<>());
		}
		
		listeners.get(eventType).add(listener);
	}
	
	@Override
	public void removeListener(EventType eventType, EventListener<E> listener) {
		List<EventListener<E>> eventListeners = listeners.get(eventType);
		
		if (eventListeners == null) {
			return;
		}
		
		eventListeners.remove(listener);
	}
	
	@Override
	public void invoke(EventType eventType, E event) {
		List<EventListener<E>> eventListeners = listeners.get(eventType);
		
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
		}
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void destroy() {
		listeners.clear();
	}
}
