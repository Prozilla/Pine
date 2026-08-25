package dev.prozilla.pine.extensions.pinet.message.request;

import dev.prozilla.pine.extensions.pinet.Pipeline;

import java.util.Collection;

/**
 * A sequential pipeline of request handlers.
 *
 * <p>
 *     A request stops going through this pipeline as soon as it is discarded by one of the request handlers.
 * </p>
 */
public class ServerRequestPipeline extends Pipeline<ServerRequestHandler> implements ServerRequestHandler {
	
	public ServerRequestPipeline() {}
	
	public ServerRequestPipeline(ServerRequestHandler... requestHandlers) {
		super(requestHandlers);
	}
	
	public ServerRequestPipeline(Collection<? extends ServerRequestHandler> requestHandlers) {
		super(requestHandlers);
	}
	
	@Override
	public void handleRequest(ServerRequest request) {
		for (ServerRequestHandler handler : this) {
			handler.handleRequest(request);
			if (request.isDiscarded()) {
				break;
			}
		}
	}
	
	@Override
	public void handleJoin(ServerRequest request) {
		for (ServerRequestHandler handler : this) {
			handler.handleJoin(request);
			if (request.isDiscarded()) {
				break;
			}
		}
	}
	
	@Override
	public void handleLeave(ServerRequest request) {
		for (ServerRequestHandler handler : this) {
			handler.handleLeave(request);
			if (request.isDiscarded()) {
				break;
			}
		}
	}
	
	/**
	 * Adds a request handler to this pipeline.
	 * @param requestHandler The handler to add
	 * @return This pipeline.
	 */
	@Override
	public ServerRequestPipeline then(ServerRequestHandler requestHandler) {
		add(requestHandler);
		return this;
	}
	
}
