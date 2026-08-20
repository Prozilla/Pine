package dev.prozilla.pine.examples.sokoban.net.connection;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;

import java.util.Queue;
import java.util.function.Consumer;

/**
 * A connection between a sender and a received, which can be used to transport {@link Packet}s.
 */
public interface Connection extends Destructible {
	
	/**
	 * Sends a packet across this connection.
	 * @param packet The packet to send
	 */
	void send(Packet packet);
	
	/**
	 * Closes this connection.
	 */
	@Override
	void destroy();
	
	static void drain(Queue<Packet> packets, Consumer<Packet> handler) {
		Packet packet;
		while ((packet = packets.poll()) != null) {
			try {
				handler.accept(packet);
			} catch (RuntimeException e) {
				Logger.system.error("Failed to handle packet: " + packet.getClass().getSimpleName(), e);
			}
		}
	}
	
}
