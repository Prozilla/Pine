package dev.prozilla.pine.extensions.pinet.message.request;

/**
 * An implementation of {@link ServerRequestHandler} that broadcasts all incoming requests.
 */
public class ServerRequestBroadcaster implements ServerRequestHandler {
	
	/**
	 * Broadcasts a request to all clients.
	 * @param request The request to broadcast
	 */
	@Override
	public void handleRequest(ServerRequest request) {
		request.replyToAll(request.getPayload());
	}
	
	@Override
	public void handleJoin(ServerRequest request) {}
	
	@Override
	public void handleLeave(ServerRequest request) {}
	
}
