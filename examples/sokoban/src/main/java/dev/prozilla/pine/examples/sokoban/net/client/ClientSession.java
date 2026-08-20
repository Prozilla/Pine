package dev.prozilla.pine.examples.sokoban.net.client;

import dev.prozilla.pine.examples.sokoban.net.Session;
import dev.prozilla.pine.examples.sokoban.net.connection.Connection;
import dev.prozilla.pine.examples.sokoban.net.connection.LocalConnection;
import dev.prozilla.pine.examples.sokoban.net.connection.RemoteConnection;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.server.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * A connection between a client and a {@link Server}, from the perspective of the client.
 */
public class ClientSession implements Session {
	
	private final Connection connection;
	private final Queue<Packet> inbound;
	private final EventLoopGroup group;
	private volatile boolean connected = true;
	
	private ClientSession(Connection connection, EventLoopGroup group) {
		this.connection = connection;
		this.group = group;
		inbound = new ConcurrentLinkedQueue<>();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	@Override
	public void markDisconnected() {
		connected = false;
	}
	
	@Override
	public void send(Packet packet) {
		connection.send(packet);
	}
	
	void receive(Packet packet) {
		inbound.add(packet);
	}
	
	public void tick(Consumer<Packet> consumer) {
		Connection.drain(inbound, consumer);
	}
	
	@Override
	public void destroy() {
		connection.destroy();
		if (group != null) {
			group.shutdownGracefully();
		}
	}
	
	public static ClientSession createRemote(String host, int port) throws IOException {
		EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		ClientConnection connection = new ClientConnection();
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) {
					RemoteConnection.configure(socketChannel.pipeline(), connection);
				}
			});
		
		try {
			bootstrap.connect(host, port).sync();
			ClientSession client = new ClientSession(connection, group);
			connection.bind(client);
			return client;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			group.shutdownGracefully();
			throw new IOException("Failed to connect to game server", e);
		}
	}
	
	public static ClientSession createLocal(LocalConnection connection) {
		ClientSession session = new ClientSession(connection, null);
		connection.bind(session::receive);
		return session;
	}
	
}
