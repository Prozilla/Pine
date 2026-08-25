package dev.prozilla.pine.extensions.pinet.message.response;

import dev.prozilla.pine.extensions.pinet.Pipeline;

import java.util.Collection;

/**
 * A sequential pipeline of response handlers.
 *
 * <p>
 *     A response stops going through this pipeline as soon as it is discarded by one of the response handlers.
 * </p>
 */
public class ServerResponsePipeline extends Pipeline<ServerResponseHandler> implements ServerResponseHandler {
	
	public ServerResponsePipeline() {}
	
	public ServerResponsePipeline(ServerResponseHandler... responseHandlers) {
		super(responseHandlers);
	}
	
	public ServerResponsePipeline(Collection<? extends ServerResponseHandler> responseHandlers) {
		super(responseHandlers);
	}
	
	@Override
	public void handleResponse(ServerResponse response) {
		for (ServerResponseHandler handler : this) {
			handler.handleResponse(response);
			if (response.isDiscarded()) {
				break;
			}
		}
	}
	
	@Override
	public void handleDisconnect(ServerResponse response) {
		for (ServerResponseHandler handler : this) {
			handler.handleDisconnect(response);
			if (response.isDiscarded()) {
				break;
			}
		}
	}
	
	/**
	 * Adds a response handler to this pipeline.
	 * @param responseHandler The handler to add
	 * @return This pipeline.
	 */
	@Override
	public ServerResponsePipeline then(ServerResponseHandler responseHandler) {
		add(responseHandler);
		return this;
	}
	
}
