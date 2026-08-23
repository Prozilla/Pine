package dev.prozilla.pine.examples.sokoban.net.server;

/**
 * Processes {@link Server.Message}s received by a {@link Server} and generates replies.
 */
public interface ServerMessageHandler {
	
	/**
	 * Processes a message sent to the server by a client.
	 * @param message The message to process
	 */
	void handleMessage(Server.Message message);
	
	void handleJoin(Server.Message message);
	
	void handleLeave(Server.Message message);
	
}
