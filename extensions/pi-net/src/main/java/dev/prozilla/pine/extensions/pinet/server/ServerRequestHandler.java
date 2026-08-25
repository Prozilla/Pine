package dev.prozilla.pine.extensions.pinet.server;

/**
 * Processes {@link Server.Request}s received by a {@link Server} and generates replies.
 */
public interface ServerRequestHandler {
	
	/**
	 * Processes a request sent to the server by a client.
	 * @param request The request to process
	 */
	void handleRequest(Server.Request request);
	
	/**
	 * Called when a client wants to join the network.
	 * @param request The request
	 */
	void handleJoin(Server.Request request);
	
	/**
	 * Called when a client wants to leave the network.
	 * @param request The request
	 */
	void handleLeave(Server.Request request);
	
}
