package dev.prozilla.pine.examples.chat.net.user;

import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.event.SimpleEventDispatcher;
import dev.prozilla.pine.common.lifecycle.Destructible;

public abstract class User implements Destructible, UserData {
	
	private final SimpleEventDispatcher<MessageEventType, String> eventDispatcher;
	
	public enum MessageEventType {
		MESSAGE_RECEIVE
	}
	
	public User() {
		eventDispatcher = new SimpleEventDispatcher();
	}
	
	public abstract void sendMessage(String message);
	
	public void receiveMessage(String message) {
		if (message == null)
			return;
		eventDispatcher.invoke(MessageEventType.MESSAGE_RECEIVE, message);
	}
	
	public void addMessageListener(EventListener<Event<MessageEventType, String>> listener) {
		eventDispatcher.addListener(MessageEventType.MESSAGE_RECEIVE, listener);
	}
	
	@Override
	public void destroy() {
		eventDispatcher.destroy();
	}
	
}
