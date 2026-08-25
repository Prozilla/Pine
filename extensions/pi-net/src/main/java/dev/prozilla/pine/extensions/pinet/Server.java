package dev.prozilla.pine.extensions.pinet;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.QueueUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.extensions.pinet.connection.LocalConnection;
import dev.prozilla.pine.extensions.pinet.connection.RemoteConnection;
import dev.prozilla.pine.extensions.pinet.connection.ServerConnection;
import dev.prozilla.pine.extensions.pinet.message.ServerMessage;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
import dev.prozilla.pine.extensions.pinet.session.ServerSession;
import dev.prozilla.pine.extensions.pinet.session.Session;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An integrated server that runs on the host and handles connections of clients to the host.
 *
 * <p>
 *     The server keeps track of the clients connected to the network and handles the communication between them, using {@link ServerSession}s.
 *     The server collects {@link ServerRequest}s from clients and queues them up to be processed by a {@link ServerRequestHandler},
 *     which may generate {@link Packet}s in response, which the server then sends back to the clients via their respective {@link ServerSession}.
 * </p>
 * @see ServerMessage
 */
public class Server implements Destructible, Synchronizable {
	
	private final Channel channel;
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	
	private final List<ServerSession> sessions;
	private ServerSession hostSession;
	private int nextClientId;
	
	private final Queue<ServerRequest> receivedRequests;
	private final ServerRequestHandler requestHandler;
	
	private Logger logger;
	
	/** The client ID of the host. */
	public static final int HOST_ID = 0;
	/** The ID of a client that is not connected to the network. */
	public static final int UNASSIGNED_ID = -1;
	
	public Server(int port, ServerRequestHandler requestHandler, PacketCodec codec, Logger logger) throws IOException {
		this.requestHandler = Checks.isNotNull(requestHandler, "requestHandler");
		this.logger = logger;
		
		sessions = new CopyOnWriteArrayList<>();
		nextClientId = HOST_ID + 1;
		receivedRequests = new ArrayDeque<>();
		
		bossGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
		workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new RemoteConnection.Initializer(this::connectClient, codec));
		
		try {
			channel = bootstrap.bind(port).sync().channel();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			throw new IOException("Failed to start server", e);
		}
	}
	
	/**
	 * Connects the host to this server.
	 * @return The connection between this server and the host
	 */
	public LocalConnection connectHost() {
		LocalConnection[] pair = LocalConnection.createPair();
		LocalConnection serverSide = pair[0];
		LocalConnection clientSide = pair[1];
		
		hostSession = new ServerSession(this, serverSide, true);
		serverSide.bind(hostSession);
		connect(hostSession);
		
		return clientSide;
	}
	
	public RemoteConnection connectClient() {
		return new ServerConnection(this);
	}
	
	/**
	 * Connects a client to this server.
	 * @param session The client to connect
	 */
	public void connect(ServerSession session) {
		sessions.add(session);
	}
	
	/**
	 * Disconnects a client from this server
	 * @param session The client to disconnect
	 */
	public void disconnect(ServerSession session) {
		sessions.remove(session);
		if (session == hostSession) {
			hostSession = null;
		}
	}
	
	/**
	 * Sends a packet to all clients, except maybe one.
	 * @param packet The packet to send
	 * @param exclude The client to exclude, or {@code null} to include all clients
	 */
	public void broadcast(Packet packet, Session exclude) {
		for (ServerSession session : sessions) {
			if (session != exclude) {
				session.send(packet);
			}
		}
	}
	
	/**
	 * Adds a request to the queue to be processed during the next synchronization.
	 * @param clientId The ID of the author of the request
	 * @param packet The payload of the request
	 * @param source The source of the request
	 */
	public void receive(int clientId, Packet packet, ServerSession source) {
		receivedRequests.add(new ServerRequest(clientId, packet, source, this));
	}
	
	/**
	 * Handles all requests that were received after the last synchronization and synchronizes each session.
	 */
	@Override
	public void synchronize() {
		QueueUtils.drain(receivedRequests, this::handleRequest);
		
		for (ServerSession session : sessions) {
			session.synchronize();
		}
	}
	
	private void handleRequest(ServerRequest request) {
		try {
			requestHandler.handleRequest(request);
		} catch (RuntimeException e) {
			getLogger().error("Failed to handle request: " + request, e);
		}
	}
	
	public ServerRequestHandler getRequestHandler() {
		return requestHandler;
	}
	
	/**
	 * @return An ID for the next client.
	 */
	public int getNextClientId() {
		return nextClientId++;
	}
	
	public Logger getLogger() {
		return logger != null ? logger : Logger.system;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
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
