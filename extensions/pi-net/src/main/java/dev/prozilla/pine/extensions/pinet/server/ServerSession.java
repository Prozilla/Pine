package dev.prozilla.pine.extensions.pinet.server;

import dev.prozilla.pine.extensions.pinet.Session;
import dev.prozilla.pine.extensions.pinet.connection.Connection;
import dev.prozilla.pine.extensions.pinet.packet.Packet;

/**
 * A connection between a client and a {@link Server}, from the perspective of the server.
 */
public class ServerSession extends Session {
	
	private final Server server;
	private final boolean isHost;
	private volatile boolean disconnectRequested;
	private boolean disconnected;
	private boolean joined;
	private int clientId = -1;
	
	public ServerSession(Server server, Connection connection, boolean isHost) {
		super(connection);
		this.server = server;
		this.isHost = isHost;
	}
	
	@Override
	public void receive(Packet packet) {
		server.receive(clientId, packet, this);
	}
	
	@Override
	public void disconnect() {
		disconnectRequested = true;
	}
	
	public void synchronize() {
		if (disconnectRequested) {
			destroy();
			return;
		}
		
		if (!joined) {
			joined = true;
			join();
		}
	}
	
	private void join() {
		clientId = isHost ? Server.HOST_ID : server.getNextClientId();
		server.getMessageHandler().handleJoin(new Server.Message(clientId, null, this, server));
	}
	
	@Override
	public void destroy() {
		if (disconnected || clientId < 0) {
			return;
		}
		disconnected = true;
		
		server.getMessageHandler().handleLeave(new Server.Message(clientId, null, this, server));
		server.disconnect(this);
		super.destroy();
	}
	
}
