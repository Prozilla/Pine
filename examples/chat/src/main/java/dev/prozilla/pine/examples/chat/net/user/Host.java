package dev.prozilla.pine.examples.chat.net.user;

import dev.prozilla.pine.examples.chat.net.server.Server;

public class Host extends User {
	
	private final Server server;
	
	public Host(Server server) {
		this.server = server;
	}
	
	@Override
	public void sendMessage(String message) {
		server.broadcastHostMessage(this, message);
	}
	
	@Override
	public String getUsername() {
		return "host";
	}
	
}
