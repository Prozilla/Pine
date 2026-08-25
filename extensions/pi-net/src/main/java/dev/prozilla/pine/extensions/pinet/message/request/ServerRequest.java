package dev.prozilla.pine.extensions.pinet.message.request;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.message.ServerMessage;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.session.ServerSession;

/**
 * A request received by the {@link Server} from a client, optionally containing a {@link Packet} as a payload.
 *
 * <p>
 *     A client (referred to as the sender in this context) can send a request to the server to ask it to do something for them.
 *     The request may contain a {@link Packet} as a payload to indicate the type of request
 *     and provide any information the server might need to process it. The server may then respond
 *     by sending {@link Packet}s to the sender and/or the other clients on the network.
 * </p>
 */
public class ServerRequest extends ServerMessage {
	
	private final int senderId;
	private final Server server;
	
	public ServerRequest(int senderId, Packet payload, ServerSession source, Server server) {
		super(payload, source);
		this.senderId = senderId;
		this.server = server;
	}
	
	/**
	 * @return The ID of the user that created this request.
	 */
	public int getSenderId() {
		return senderId;
	}
	
	/**
	 * @return The server that received this request.
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * @return {@code true} if this request was created by the host.
	 */
	public boolean isSentByHost() {
		return senderId == Server.HOST_ID;
	}
	
	/**
	 * Sends a packet as a reply to this request to all clients and marks this request as acknowledged.
	 * @param packet The packet to send
	 */
	public void replyToAll(Packet packet) {
		acknowledge();
		if (server != null) {
			server.broadcast(packet, null);
		}
	}
	
	/**
	 * Sends a packet as a reply to this request to all clients, except the sender of this request,
	 * and marks this request as acknowledged.
	 * @param packet The packet to send
	 */
	public void replyToOthers(Packet packet) {
		acknowledge();
		if (server != null) {
			server.broadcast(packet, source);
		}
	}
	
	@Override
	public String toString() {
		return Printable.objectToString(this,
			"senderId", getSenderId(),
			"payload", getPayload(),
			"source", source,
			"server", getServer(),
			"isAcknowledged", isAcknowledged(),
			"isDiscarded", isDiscarded());
	}
	
}
