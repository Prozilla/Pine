package dev.prozilla.pine.extensions.pinet.session;

import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.connection.Connection;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
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
	private int clientId;
	
	public ServerSession(Server server, Connection connection, boolean isHost) {
		super(connection, server.getLogger());
		this.server = server;
		this.isHost = isHost;
		clientId = Server.UNASSIGNED_ID;
	}
	
	@Override
	public void receive(Packet packet) {
		server.receive(clientId, packet, this);
	}
	
	@Override
	public void disconnect() {
		disconnectRequested = true;
	}
	
	@Override
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
		try {
			server.getRequestHandler().handleJoin(new ServerRequest(clientId, null, this, server));
		} catch (RuntimeException e) {
			getLogger().error("Failed to handle request to join", e);
		}
	}
	
	@Override
	public void destroy() {
		if (disconnected || clientId == Server.UNASSIGNED_ID) {
			return;
		}
		disconnected = true;
		
		try {
			server.getRequestHandler().handleLeave(new ServerRequest(clientId, null, this, server));
		} catch (RuntimeException e) {
			getLogger().error("Failed to handle request to leave", e);
		}
		server.disconnect(this);
		super.destroy();
	}
	
}
