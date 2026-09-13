package dev.prozilla.pine.extensions.pinet.packet;

import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;

/**
 * The payload of a message between a client and a {@link Server} that is encoded into a {@link PacketBuffer} by the sender and decoded by the receiver.
 *
 * <p>
 *     When the server or a client receives a packet, it is wrapped in a {@link ServerRequest} or a {@link ServerResponse} respectively,
 *     and passed to a {@link ServerRequestHandler} or a {@link ServerResponseHandler} respectively.
 * </p>
 */
public interface Packet {
	
	/**
	 * @return The unique ID of this type of packet.
	 */
	int getPacketId();
	
	/**
	 * Encodes the data of this packet into a {@link PacketBuffer}.
	 * @param buffer The buffer to write to
	 */
	void encode(PacketBuffer buffer);
	
}
