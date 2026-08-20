package dev.prozilla.pine.examples.sokoban.net.connection;

import dev.prozilla.pine.examples.sokoban.net.packet.Packet;

import java.util.function.Consumer;

public class LocalConnection implements Connection {
	
	private Consumer<Packet> receiver;
	private volatile boolean open = true;
	private LocalConnection peer;
	
	public void bind(Consumer<Packet> receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public void send(Packet packet) {
		LocalConnection target = peer;
		if (open && target != null && target.open && target.receiver != null) {
			target.receiver.accept(packet);
		}
	}
	
	@Override
	public void destroy() {
		open = false;
		
		if (peer != null) {
			peer.open = false;
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
