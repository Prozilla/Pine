package dev.prozilla.pine.extensions.pinet.message;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestPipeline;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponsePipeline;

import java.util.function.Predicate;

/**
 * A message handler that discards messages that do not match a given predicate.
 *
 * <p>
 *     When used in a {@link ServerRequestPipeline} or {@link ServerResponsePipeline}, this filter
 *     will only pass messages that match the given predicate to the next message handler in the pipeline and block all other messages.
 * </p>
 */
public class ServerMessageFilter implements ServerMessageAdapter {
	
	private final Predicate<ServerMessage> predicate;
	
	public ServerMessageFilter(Predicate<ServerMessage> predicate) {
		this.predicate = Checks.isNotNull(predicate, "predicate");
	}
	
	@Override
	public void handleMessage(ServerMessage message) {
		if (!predicate.test(message)) {
			message.discard();
		}
	}
	
	/**
	 * Creates a message filter that only passes messages to the next handler in the pipeline that have not been acknowledged yet.
	 * @return The new filter.
	 */
	public static ServerMessageFilter unacknowledged() {
		return new ServerMessageFilter(Predicate.not(ServerMessage::isAcknowledged));
	}
	
	/**
	 * Creates a message filter that only passes messages to the next handler in the pipeline that have a payload attached to them.
	 * @return The new filter.
	 */
	public static ServerMessageFilter hasPayload() {
		return new ServerMessageFilter((message) -> message.getPayload() != null);
	}
	
}
