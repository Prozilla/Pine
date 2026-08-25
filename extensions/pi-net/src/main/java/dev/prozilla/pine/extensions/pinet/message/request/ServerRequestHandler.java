package dev.prozilla.pine.extensions.pinet.message.request;

import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.message.ServerMessagePipeline;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;

/**
 * Handles {@link ServerRequest}s received by a {@link Server} and generates replies.
 */
public interface ServerRequestHandler {
	
	/**
	 * Called when the server receives a request from a client.
	 * @param request The received request
	 */
	void handleRequest(ServerRequest request);
	
	/**
	 * Called when a client wants to join the network.
	 */
	void handleJoin(ServerRequest request);
	
	/**
	 * Called when a client wants to leave the network.
	 */
	void handleLeave(ServerRequest request);
	
	/**
	 * Creates a pipeline that pipes requests into the given handler.
	 * @param requestHandler The handler to pipe requests to
	 * @return The new pipeline.
	 */
	default ServerRequestPipeline then(ServerRequestHandler requestHandler) {
		return new ServerRequestPipeline(this, requestHandler);
	}
	
	/**
	 * Creates a new pipeline combining this request handler with the given response handler.
	 * @return The new pipeline.
	 */
	default ServerMessagePipeline and(ServerResponseHandler responseHandler) {
		return new ServerMessagePipeline(this, responseHandler);
	}
	
}
