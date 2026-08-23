package dev.prozilla.pine.examples.sokoban.net.client;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.QueueUtils;
import dev.prozilla.pine.examples.sokoban.net.Session;
import dev.prozilla.pine.examples.sokoban.net.connection.Connection;
import dev.prozilla.pine.examples.sokoban.net.connection.LocalConnection;
import dev.prozilla.pine.examples.sokoban.net.connection.RemoteConnection;
import dev.prozilla.pine.examples.sokoban.net.connection.RemoteConnectionInitializer;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.packet.PacketCodec;
import dev.prozilla.pine.examples.sokoban.net.server.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A connection between a client and a {@link Server}, from the perspective of the client.
 */
public class ClientSession extends Session {
	
	private final EventLoopGroup group;
	private volatile boolean isConnected;
	
	private final Queue<Packet> receivedPackets;
	private final ClientPacketHandler packetHandler;
	
	private ClientSession(Connection connection, ClientPacketHandler packetHandler, EventLoopGroup group) {
		super(connection);
		this.packetHandler = packetHandler;
		this.group = group;
		receivedPackets = new ConcurrentLinkedQueue<>();
		isConnected = true;
	}
	
	@Override
	public void receive(Packet packet) {
		receivedPackets.add(packet);
	}
	
	@Override
	public void disconnect() {
		destroy();
	}
	
	public void synchronize() {
		QueueUtils.drain(receivedPackets, this::handlePacket);
	}
	
	private void handlePacket(Packet packet) {
		try {
			packetHandler.handlePacket(packet);
		} catch (RuntimeException e) {
			Logger.system.error("Failed to handle packet: " + packet.getClass().getSimpleName(), e);
		}
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	@Override
	public void destroy() {
		isConnected = false;
		super.destroy();
		if (group != null) {
			group.shutdownGracefully();
		}
	}
	
	public static ClientSession createRemote(String host, int port, ClientPacketHandler packetHandler, PacketCodec codec) throws IOException {
		EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		RemoteConnection connection = new RemoteConnection();
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new RemoteConnectionInitializer(connection, codec));
		
		try {
			bootstrap.connect(host, port).sync();
			ClientSession client = new ClientSession(connection, packetHandler, group);
			connection.bind(client);
			return client;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			group.shutdownGracefully();
			throw new IOException("Failed to connect to game server", e);
		}
	}
	
	public static ClientSession createLocal(LocalConnection connection, ClientPacketHandler packetHandler) {
		ClientSession session = new ClientSession(connection, packetHandler, null);
		connection.bind(session);
		return session;
	}
	
}
