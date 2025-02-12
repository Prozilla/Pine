package dev.prozilla.pine.common.event;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcher<Event extends Enum<Event>> {
	
	private final Map<Event, List<EventListener>> listeners;
	
	private final Application application;
	private final Logger logger;
	
	public EventDispatcher(Application application) {
		this.application = application;
		logger = application.getLogger();
		
		listeners = new HashMap<>();
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
				logger.error("Event listener failed", e);
			}
		}
	}
	
	public void destroy() {
		listeners.clear();
	}
}
