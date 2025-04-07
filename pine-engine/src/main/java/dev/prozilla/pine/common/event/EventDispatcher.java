package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcher<Event extends Enum<Event>> implements EventDispatcherContext<Event> {
	
	private final Map<Event, List<EventListener>> listeners;
	
	protected Logger logger;
	
	public EventDispatcher() {
		listeners = new HashMap<>();
		logger = null;
	}
	
	@Override
	public void addListener(Event eventType, EventListener listener) {
		if (!listeners.containsKey(eventType)) {
			listeners.put(eventType, new ArrayList<>());
		}
		
		listeners.get(eventType).add(listener);
	}
	
	@Override
	public void removeListener(Event eventType, EventListener listener) {
		List<EventListener> eventListeners = listeners.get(eventType);
		
		if (eventListeners == null) {
			return;
		}
		
		eventListeners.remove(listener);
	}
	
	@Override
	public void invoke(Event eventType) {
		List<EventListener> eventListeners = listeners.get(eventType);
		
		if (eventListeners == null) {
			return;
		}
		
		for (EventListener listener : eventListeners) {
			try {
				listener.execute();
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
