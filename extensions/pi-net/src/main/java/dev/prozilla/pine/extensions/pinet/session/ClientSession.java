package dev.prozilla.pine.extensions.pinet.session;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.QueueUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.connection.Connection;
import dev.prozilla.pine.extensions.pinet.connection.LocalConnection;
import dev.prozilla.pine.extensions.pinet.connection.RemoteConnection;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
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
	
	private final Queue<ServerResponse> receivedResponses;
	private final ServerResponseHandler responseHandler;
	private boolean isDisconnectHandled;
	
	private ClientSession(Connection connection, ServerResponseHandler responseHandler, EventLoopGroup group, Logger logger) {
		super(connection, logger);
		this.responseHandler = Checks.isNotNull(responseHandler, "responseHandler");
		this.group = group;
		receivedResponses = new ConcurrentLinkedQueue<>();
		isConnected = true;
		isDisconnectHandled = false;
	}
	
	@Override
	public void receive(Packet packet) {
		receivedResponses.add(new ServerResponse(packet, this));
	}
	
	@Override
	public void disconnect() {
		destroy();
	}
	
	/**
	 * Handles all responses that were received after the last synchronization and checks if this session is still connected.
	 */
	@Override
	public void synchronize() {
		QueueUtils.drain(receivedResponses, this::handleResponse);
		
		if (!isConnected && !isDisconnectHandled) {
			isDisconnectHandled = true;
			try {
				responseHandler.handleDisconnect(new ServerResponse(null, null));
			} catch (RuntimeException e) {
				getLogger().error("Failed to handle disconnect", e);
			}
		}
	}
	
	private void handleResponse(ServerResponse response) {
		try {
			responseHandler.handleResponse(response);
		} catch (RuntimeException e) {
			getLogger().error("Failed to handle response: " + response, e);
		}
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	@Override
	public void destroy() {
		if (!isConnected) {
			return;
		}
		isConnected = false;
		super.destroy();
		if (group != null) {
			group.shutdownGracefully();
		}
	}
	
	public static ClientSession createRemote(String host, int port, ServerResponseHandler responseHandler, PacketCodec codec, Logger logger) throws IOException {
		EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		RemoteConnection connection = new RemoteConnection();
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new RemoteConnection.Initializer(() -> connection, codec));
		
		try {
			bootstrap.connect(host, port).sync();
			ClientSession client = new ClientSession(connection, responseHandler, group, logger);
			connection.bind(client);
			return client;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			group.shutdownGracefully();
			throw new IOException("Failed to create remote connection", e);
		}
	}
	
	public static ClientSession createLocal(LocalConnection connection, ServerResponseHandler responseHandler, Logger logger) {
		ClientSession session = new ClientSession(connection, responseHandler, null, logger);
		connection.bind(session);
		return session;
	}
	
}
