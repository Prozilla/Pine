package dev.prozilla.pine.extensions.pinet.packet;

import dev.prozilla.pine.extensions.pinet.server.Server;

/**
 * Represents a message between a client and a {@link Server} that is encoded into a {@link PacketBuffer} by the sender and decoded by the receiver.
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
