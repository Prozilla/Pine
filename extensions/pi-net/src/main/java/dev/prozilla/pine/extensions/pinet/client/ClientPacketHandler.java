package dev.prozilla.pine.extensions.pinet.client;


import dev.prozilla.pine.extensions.pinet.packet.Packet;

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
