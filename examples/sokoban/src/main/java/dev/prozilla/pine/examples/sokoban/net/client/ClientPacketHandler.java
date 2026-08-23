package dev.prozilla.pine.examples.sokoban.net.client;

import dev.prozilla.pine.examples.sokoban.net.packet.Packet;

/**
 * Processes {@link Packet}s received by a client.
 */
public interface ClientPacketHandler {
	
	/**
	 * Processes a packet sent to a client by the server.
	 * @param packet The packet to process
	 */
	void handlePacket(Packet packet);
	
}
