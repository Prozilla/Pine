package dev.prozilla.pine.examples.sokoban.net.server;

import dev.prozilla.pine.examples.sokoban.net.Session;
import dev.prozilla.pine.examples.sokoban.net.connection.Connection;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.system.RequestProcessor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A connection between a client and a {@link Server}, from the perspective of the server.
 */
public class ServerSession implements Session, RequestProcessor.Responder {
	
	private final Server server;
	private final Connection connection;
	private final RequestProcessor processor;
	private final boolean host;
	private final Queue<Packet> inbound;
	private volatile boolean disconnectRequested;
	private boolean disconnected;
	private boolean joined;
	private int playerId = -1;
	
	public ServerSession(Server server, Connection connection, RequestProcessor processor, boolean host) {
		this.server = server;
		this.connection = connection;
		this.processor = processor;
		this.host = host;
		inbound = new ConcurrentLinkedQueue<>();
	}
	
	public void enqueue(Packet packet) {
		inbound.add(packet);
	}
	
	public void markDisconnected() {
		disconnectRequested = true;
	}
	
	public void tick() {
		if (disconnectRequested) {
			destroy();
			return;
		}
		
		if (!joined) {
			joined = true;
			join();
		}
		
		Connection.drain(inbound, this::onPacket);
	}
	
	private void join() {
		playerId = processor.onJoin(host, this);
	}
	
	private void onPacket(Packet packet) {
		processor.receive(playerId, packet, this);
	}
	
	@Override
	public void send(Packet packet) {
		connection.send(packet);
	}
	
	@Override
	public void broadcast(Packet packet) {
		server.broadcast(packet, null);
	}
	
	@Override
	public void broadcastOthers(Packet packet) {
		server.broadcast(packet, this);
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	@Override
	public void destroy() {
		if (disconnected || playerId < 0) {
			return;
		}
		disconnected = true;
		
		processor.onLeave(playerId, this);
		server.disconnect(this);
		connection.destroy();
	}
	
}
