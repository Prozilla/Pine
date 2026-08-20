package dev.prozilla.pine.examples.sokoban.net.packet;

import dev.prozilla.pine.examples.sokoban.net.server.Server;

/**
 * Represents a message between a client and a {@link Server} that is written into a {@link PacketBuffer} by the sender and read from a {@link PacketBuffer} by the receiver.
 */
public interface Packet {
	
	/**
	 * @return The unique ID of this type of packet.
	 */
	int getPacketId();
	
	/**
	 * Writes the data of this packet into a {@link PacketBuffer}.
	 * @param buffer The buffer to write to
	 */
	void write(PacketBuffer buffer);
	
}
