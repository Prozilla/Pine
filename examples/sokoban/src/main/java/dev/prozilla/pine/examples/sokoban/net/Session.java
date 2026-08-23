package dev.prozilla.pine.examples.sokoban.net;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.examples.sokoban.net.connection.Connection;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.server.Server;

/**
 * A connection between a client and a {@link Server}.
 */
public abstract class Session implements Destructible {
	
	private final Connection connection;
	
	public Session(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Sends a packet over the connection associated with this session.
	 * @param packet The packet to send
	 */
	public void send(Packet packet) {
		connection.send(packet);
	}
	
	/**
	 * Called when a packet is received over the connection associated with this session.
	 * @param packet The packet to receive
	 */
	public abstract void receive(Packet packet);
	
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Disconnects the client associated with this session from the {@link Server}.
	 */
	public abstract void disconnect();
	
	@Override
	public void destroy() {
		connection.destroy();
	}
	
}
