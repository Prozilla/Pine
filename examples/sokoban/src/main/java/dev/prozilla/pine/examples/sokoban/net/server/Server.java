package dev.prozilla.pine.examples.sokoban.net.server;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.examples.sokoban.net.connection.LocalConnection;
import dev.prozilla.pine.examples.sokoban.net.connection.RemoteConnection;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.system.RequestProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An integrated server that runs on the host and handles connections of clients to the host.
 */
public class Server implements Destructible {
	
	private final RequestProcessor processor;
	private final List<ServerSession> sessions;
	private final Channel channel;
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	private ServerSession hostSession;
	
	public Server(int port, RequestProcessor processor) throws IOException {
		this.processor = processor;
		sessions = new CopyOnWriteArrayList<>();
		
		bossGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
		workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) {
					RemoteConnection.configure(socketChannel.pipeline(), new ServerConnection(Server.this, processor));
				}
			});
		
		try {
			channel = bootstrap.bind(port).sync().channel();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			throw new IOException("Failed to start server", e);
		}
	}
	
	public LocalConnection connectHost() {
		LocalConnection[] pair = LocalConnection.createPair();
		LocalConnection serverSide = pair[0];
		LocalConnection clientSide = pair[1];
		
		hostSession = new ServerSession(this, serverSide, processor, true);
		serverSide.bind(hostSession::enqueue);
		connect(hostSession);
		
		return clientSide;
	}
	
	public void connect(ServerSession session) {
		sessions.add(session);
	}
	
	public void disconnect(ServerSession session) {
		sessions.remove(session);
		if (session == hostSession) {
			hostSession = null;
		}
	}
	
	public void broadcast(Packet packet, ServerSession exclude) {
		for (ServerSession session : sessions) {
			if (session != exclude) {
				session.send(packet);
			}
		}
	}
	
	public void tick() {
		for (ServerSession session : sessions) {
			session.tick();
		}
	}
	
	/**
	 * Disconnects all clients and closes this server.
	 */
	@Override
	public void destroy() {
		for (ServerSession session : new ArrayList<>(sessions)) {
			session.getConnection().destroy();
		}
		sessions.clear();
		channel.close();
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	
}
