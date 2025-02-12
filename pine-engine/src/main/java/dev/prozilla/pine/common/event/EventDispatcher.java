package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcher<Event extends Enum<Event>> {
	
	private final Map<Event, List<EventListener>> listeners;
	
	protected Logger logger;
	
	public EventDispatcher() {
		listeners = new HashMap<>();
		logger = null;
	}
	
	public void addListener(Event eventName, EventListener listener) {
		if (!listeners.containsKey(eventName)) {
			listeners.put(eventName, new ArrayList<>());
		}
		
		listeners.get(eventName).add(listener);
	}
	
	public void removeListener(Event eventName, EventListener listener) {
		List<EventListener> eventListeners = listeners.get(eventName);
		
		if (eventListeners == null) {
			return;
		}
		
		eventListeners.remove(listener);
	}
	
	public void invoke(Event eventName) {
		List<EventListener> eventListeners = listeners.get(eventName);
		
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
