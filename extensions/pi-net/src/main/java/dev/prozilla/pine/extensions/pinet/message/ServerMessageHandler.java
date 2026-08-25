package dev.prozilla.pine.extensions.pinet.message;

import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;

/**
 * Handles {@link ServerMessage}s received by a client or the {@link Server} and generates replies.
 */
public interface ServerMessageHandler extends ServerRequestHandler, ServerResponseHandler {
	
	/**
	 * Creates a pipeline that pipes messages into the given handler.
	 * @param messageHandler The handler to pipe messages to
	 * @return The new pipeline.
	 */
	default ServerMessagePipeline then(ServerMessageHandler messageHandler) {
		return new ServerMessagePipeline(this, messageHandler);
	}
	
}
