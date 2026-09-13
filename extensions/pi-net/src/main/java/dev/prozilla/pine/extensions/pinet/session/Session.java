package dev.prozilla.pine.extensions.pinet.session;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.Synchronizable;
import dev.prozilla.pine.extensions.pinet.connection.Connection;
import dev.prozilla.pine.extensions.pinet.packet.Packet;

/**
 * A connection between a client and the {@link Server}.
 */
public abstract class Session implements Destructible, Synchronizable {
	
	private final Connection connection;
	private Logger logger;
	
	public Session(Connection connection, Logger logger) {
		this.connection = connection;
		this.logger = logger;
	}
	
	/**
	 * Sends a packet over the connection associated with this session.
	 * @param packet The packet to send
	 */
	public void send(Packet packet) {
		Checks.isNotNull(packet, "packet");
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
	
	public Logger getLogger() {
		return logger != null ? logger : Logger.system;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public void destroy() {
		connection.destroy();
	}
	
}
