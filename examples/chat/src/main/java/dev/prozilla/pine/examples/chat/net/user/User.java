package dev.prozilla.pine.examples.chat.net.user;

import dev.prozilla.pine.common.event.EventDispatcher;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.lifecycle.Destructible;

public abstract class User implements Destructible, UserData {
	
	private final EventDispatcher<MessageEvent, String> eventDispatcher;
	
	public enum MessageEvent {
		MESSAGE_RECEIVE
	}
	
	public User() {
		eventDispatcher = new EventDispatcher<>();
	}
	
	public abstract void sendMessage(String message);
	
	public void receiveMessage(String message) {
		if (message == null)
			return;
		eventDispatcher.invoke(MessageEvent.MESSAGE_RECEIVE, message);
	}
	
	public void addMessageListener(EventListener<String> listener) {
		eventDispatcher.addListener(MessageEvent.MESSAGE_RECEIVE, listener);
	}
	
	@Override
	public void destroy() {
		eventDispatcher.destroy();
	}
	
}
