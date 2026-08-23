package dev.prozilla.pine.examples.sokoban.net.connection;

import dev.prozilla.pine.examples.sokoban.net.Session;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;

public class LocalConnection implements Connection {
	
	private Session session;
	private LocalConnection peer;
	private volatile boolean isConnected = true;
	
	@Override
	public void send(Packet packet) {
		if (isConnected && peer != null && peer.isConnected && peer.session != null) {
			peer.session.receive(packet);
		}
	}
	
	@Override
	public void bind(Session session) {
		this.session = session;
	}
	
	@Override
	public void destroy() {
		isConnected = false;
		
		if (peer != null) {
			peer.isConnected = false;
		}
	}
	
	public static LocalConnection[] createPair() {
		LocalConnection connectionA = new LocalConnection();
		LocalConnection connectionB = new LocalConnection();
		connectionA.peer = connectionB;
		connectionB.peer = connectionA;
		return new LocalConnection[] { connectionA, connectionB };
	}
	
}
