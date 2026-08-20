package dev.prozilla.pine.examples.sokoban.net;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.server.Server;

/**
 * A connection between a client and a {@link Server}.
 */
public interface Session extends Destructible {
	
	/**
	 * Sends a packet over the connection associated with this session.
	 * @param packet The packet to send
	 */
	void send(Packet packet);
	
	/**
	 * Marks the client as disconnected from the {@link Server}.
	 */
	void markDisconnected();
	
}
