package dev.prozilla.pine.extensions.pinet.message;

import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;

/**
 * A message handler that handles all messages equally, regardless of their type.
 */
@FunctionalInterface
public interface ServerMessageAdapter extends ServerMessageHandler {
	
	@Override
	default void handleRequest(ServerRequest request) {
		handleMessage(request);
	}
	
	@Override
	default void handleJoin(ServerRequest request) {
		handleMessage(request);
	}
	
	@Override
	default void handleLeave(ServerRequest request) {
		handleMessage(request);
	}
	
	@Override
	default void handleResponse(ServerResponse response) {
		handleMessage(response);
	}
	
	@Override
	default void handleDisconnect(ServerResponse response) {
		handleMessage(response);
	}
	
	/**
	 * Called when a client or the server receives a message.
	 * @param message The received message
	 */
	void handleMessage(ServerMessage message);
	
}
