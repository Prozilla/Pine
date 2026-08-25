package dev.prozilla.pine.extensions.pinet.server;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.QueueUtils;
import dev.prozilla.pine.extensions.pinet.connection.LocalConnection;
import dev.prozilla.pine.extensions.pinet.connection.RemoteConnection;
import dev.prozilla.pine.extensions.pinet.connection.RemoteConnectionInitializer;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
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
 *     The server collects {@link Request}s from clients and queues them up to be processed by a {@link ServerRequestHandler},
 *     which may generate {@link Packet}s in response, which the server then sends back to the clients via their respective {@link ServerSession}.
 * </p>
 */
public class Server implements Destructible {
	
	private final Channel channel;
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	
	private final List<ServerSession> sessions;
	private ServerSession hostSession;
	private int nextClientId;
	
	private final Queue<Request> receivedRequests;
	private final ServerRequestHandler requestHandler;
	
	/** The client ID of the host. */
	public static final int HOST_ID = 0;
	
	public Server(int port, ServerRequestHandler requestHandler, PacketCodec codec) throws IOException {
		this.requestHandler = requestHandler;
		sessions = new CopyOnWriteArrayList<>();
		nextClientId = HOST_ID + 1;
		receivedRequests = new ArrayDeque<>();
		
		bossGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
		workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		RemoteConnection connection = new ServerConnection(this);
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new RemoteConnectionInitializer(connection, codec));
		
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
	public void broadcast(Packet packet, ServerSession exclude) {
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
		receivedRequests.add(new Request(clientId, packet, source, this));
	}
	
	/**
	 * Processes all requests that were received after the last synchronization and synchronizes each session.
	 */
	public void synchronize() {
		QueueUtils.drain(receivedRequests, this::handleRequest);
		
		for (ServerSession session : sessions) {
			session.synchronize();
		}
	}
	
	private void handleRequest(Request request) {
		try {
			requestHandler.handleRequest(request);
		} catch (RuntimeException e) {
			Logger.system.error("Failed to handle request: " + request.getPayload().getClass().getSimpleName(), e);
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

	/**
	 * A request received by the {@link Server} from a client, optionally containing a {@link Packet} as a payload.
	 *
	 * <p>
	 *     A client (referred to as the author in this context) can send a request to the server to ask it to do something for them.
	 *     The request may contain a {@link Packet} as a payload to indicate the type of request
	 *     and provide any information the server might need to process it. The server may then respond
	 *     by sending {@link Packet}s to the author and/or the other clients on the network.
	 * </p>
	 */
	public static final class Request {
		
		private final int authorId;
		private final Packet payload;
		private final ServerSession source;
		private final Server server;
		
		public Request(Packet payload) {
			this(Server.HOST_ID, payload, null, null);
		}
		
		public Request(int authorId, Packet payload, ServerSession source, Server server) {
			this.authorId = authorId;
			this.payload = payload;
			this.source = source;
			this.server = server;
		}
		
		/**
		 * @return The ID of the user that created this request.
		 */
		public int getAuthorId() {
			return authorId;
		}
		
		/**
		 * @return The payload attached to this request, or {@code null}.
		 */
		public Packet getPayload() {
			return payload;
		}
		
		/**
		 * @return The server that received this request.
		 */
		public Server getServer() {
			return server;
		}
		
		public boolean isLocal() {
			return source == null;
		}
		
		/**
		 * @return {@code true} if this request was created by the host.
		 */
		public boolean receivedFromHost() {
			return authorId == Server.HOST_ID;
		}
		
		/**
		 * Sends a packet as a reply to this request to its author.
		 * @param packet The packet to send
		 */
		public void reply(Packet packet) {
			if (source != null) {
				source.send(packet);
			}
		}
		
		/**
		 * Sends a packet as a reply to this request to all clients.
		 * @param packet The packet to send
		 */
		public void replyToAll(Packet packet) {
			if (server != null) {
				server.broadcast(packet, null);
			}
		}
		
		/**
		 * Sends a packet as a reply to this request to all clients, except the author of this request.
		 * @param packet The packet to send
		 */
		public void replyToOthers(Packet packet) {
			if (server != null) {
				server.broadcast(packet, source);
			}
		}
	}
}
