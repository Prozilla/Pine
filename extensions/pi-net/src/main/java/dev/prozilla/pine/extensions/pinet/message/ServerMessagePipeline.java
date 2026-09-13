package dev.prozilla.pine.extensions.pinet.message;

import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestPipeline;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponsePipeline;

/**
 * A combination of a {@link ServerRequestPipeline} and a {@link ServerResponsePipeline} that handles messages by delegating to the corresponding pipeline.
 *
 * <p>
 *     This pipeline can be used to handle all incoming messages on both the server and on the clients.
 * </p>
 */
public class ServerMessagePipeline implements ServerMessageHandler {
	
	private final ServerRequestPipeline requestPipeline;
	private final ServerResponsePipeline responsePipeline;
	
	public ServerMessagePipeline() {
		this(new ServerRequestPipeline(), new ServerResponsePipeline());
	}
	
	public ServerMessagePipeline(ServerMessageHandler messageHandlerA, ServerMessageHandler messageHandlerB) {
		this(new ServerRequestPipeline(messageHandlerA, messageHandlerB), new ServerResponsePipeline(messageHandlerA, messageHandlerB));
	}
	
	public ServerMessagePipeline(ServerRequestHandler requestHandler, ServerResponseHandler responseHandler) {
		this(new ServerRequestPipeline(requestHandler), new ServerResponsePipeline(responseHandler));
	}
	
	public ServerMessagePipeline(ServerRequestPipeline requestPipeline, ServerResponsePipeline responsePipeline) {
		this.requestPipeline = requestPipeline;
		this.responsePipeline = responsePipeline;
	}
	
	@Override
	public void handleRequest(ServerRequest request) {
		requestPipeline.handleRequest(request);
	}
	
	@Override
	public void handleJoin(ServerRequest request) {
		requestPipeline.handleJoin(request);
	}
	
	@Override
	public void handleLeave(ServerRequest request) {
		requestPipeline.handleLeave(request);
	}
	
	@Override
	public void handleResponse(ServerResponse response) {
		responsePipeline.handleResponse(response);
	}
	
	@Override
	public void handleDisconnect(ServerResponse response) {
		responsePipeline.handleDisconnect(response);
	}
	
	/**
	 * Adds a request handler to this pipeline.
	 * @param requestHandler The handler to add
	 * @return This pipeline.
	 */
	@Override
	public ServerRequestPipeline then(ServerRequestHandler requestHandler) {
		return requestPipeline.then(requestHandler);
	}
	
	/**
	 * Adds a response handler to this pipeline.
	 * @param responseHandler The handler to add
	 * @return This pipeline.
	 */
	@Override
	public ServerResponsePipeline then(ServerResponseHandler responseHandler) {
		return responsePipeline.then(responseHandler);
	}
	
	/**
	 * Adds a message handler to this pipeline.
	 * @return This pipeline.
	 */
	@Override
	public ServerMessagePipeline then(ServerMessageHandler messageHandler) {
		requestPipeline.add(messageHandler);
		responsePipeline.add(messageHandler);
		return this;
	}
	
	/**
	 * Adds a request handler to this pipeline.
	 * @param requestHandler The handler to add
	 * @return This pipeline.
	 */
	@Override
	public ServerMessagePipeline and(ServerRequestHandler requestHandler) {
		requestPipeline.add(requestHandler);
		return this;
	}
	
	/**
	 * Adds a response handler to this pipeline.
	 * @param responseHandler The handler to add
	 * @return This pipeline.
	 */
	@Override
	public ServerMessagePipeline and(ServerResponseHandler responseHandler) {
		responsePipeline.add(responseHandler);
		return this;
	}
	
	public ServerMessagePipeline clear() {
		requestPipeline.clear();
		responsePipeline.clear();
		return this;
	}
	
	public boolean isEmpty() {
		return requestPipeline.isEmpty() && responsePipeline.isEmpty();
	}
	
}
