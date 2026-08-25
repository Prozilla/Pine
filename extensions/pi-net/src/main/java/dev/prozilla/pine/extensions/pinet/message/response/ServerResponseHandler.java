package dev.prozilla.pine.extensions.pinet.message.response;


import dev.prozilla.pine.extensions.pinet.message.ServerMessagePipeline;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;

/**
 * Handles {@link ServerResponse}s received by a client and generates replies.
 */
public interface ServerResponseHandler {
	
	/**
	 * Called when a client receives a message from the server.
	 * @param response The received response
	 */
	void handleResponse(ServerResponse response);
	
	/**
	 * Called when the client is disconnected from the network.
	 */
	void handleDisconnect(ServerResponse response);
	
	/**
	 * Creates a pipeline that pipes responses into the given handler.
	 * @param responseHandler The handler to pipe responses to
	 * @return The new pipeline.
	 */
	default ServerResponsePipeline then(ServerResponseHandler responseHandler) {
		return new ServerResponsePipeline(this, responseHandler);
	}
	
	/**
	 * Creates a new pipeline combining this response handler with the given request handler.
	 * @return The new pipeline.
	 */
	default ServerMessagePipeline and(ServerRequestHandler requestHandler) {
		return new ServerMessagePipeline(requestHandler, this);
	}
	
}
