package dev.prozilla.pine.extensions.pinet.connection;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.extensions.pinet.Session;
import dev.prozilla.pine.extensions.pinet.packet.Packet;

/**
 * A connection between a sender and a received, which can be used to transport {@link Packet}s.
 */
public interface Connection extends Destructible {
	
	/**
	 * Sends a packet across this connection.
	 * @param packet The packet to send
	 */
	void send(Packet packet);
	
	void bind(Session session);
	
	/**
	 * Closes this connection.
	 */
	@Override
	void destroy();
	
}
