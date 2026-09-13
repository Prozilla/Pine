package dev.prozilla.pine.extensions.pinet.component;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.Synchronizable;
import dev.prozilla.pine.extensions.pinet.message.ServerMessageHandler;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
import dev.prozilla.pine.extensions.pinet.session.ClientSession;

import java.io.IOException;

/**
 * Manages a client's connection to a network.
 * <p>
 *     The network can either be a {@link Server}, in which case the connection is local if the client is the host and otherwise remote,
 *     or a standalone network, in which case the "client" has no connection and {@link Packet}s get sent directly to a {@link ServerRequestHandler}.
 * </p>
 * <p>
 *     This makes it possible to treat all different modes equally and to run the same logic,
 *     regardless of whether there is any connection and the type of connection.
 * </p>
 */
public class NetworkManager extends Component implements Synchronizable {
	
	private Server server;
	private ClientSession session;
	private PacketCodec codec;
	private int localClientId;
	
	private ServerRequestHandler standaloneRequestHandler;
	private ServerResponseHandler standaloneResponseHandler;
	
	public NetworkManager() {
		this(new PacketCodec());
	}
	
	public NetworkManager(PacketCodec codec) {
		this.codec = codec;
		localClientId = Server.UNASSIGNED_ID;
	}
	
	/**
	 * Creates an integrated server and a local connection to it.
	 */
	public void createHost(int port, ServerMessageHandler messageHandler) {
		createHost(port, messageHandler, messageHandler);
	}
	
	/**
	 * Creates an integrated server and a local connection to it.
	 */
	public void createHost(int port, ServerRequestHandler requestHandler, ServerResponseHandler responseHandler) {
		disconnect();
		try {
			server = new Server(port, requestHandler, codec, getLogger());
			session = ClientSession.createLocal(server.connectHost(), responseHandler, getLogger());
		} catch (IOException e) {
			getLogger().error("Failed to start server", e);
		}
	}
	
	/**
	 * Creates a connection to a remote server.
	 * @param address The address of the server
	 */
	public void createClient(String address, int port, ServerResponseHandler responseHandler) {
		disconnect();
		try {
			session = ClientSession.createRemote(address, port, responseHandler, codec, getLogger());
		} catch (IOException e) {
			getLogger().error("Failed to connect to server", e);
		}
	}
	
	/**
	 * Creates a standalone network that sends messages directly to the handler.
	 */
	public void createStandalone(ServerMessageHandler messageHandler) {
		createStandalone(messageHandler, messageHandler);
	}
	
	/**
	 * Creates a standalone network that sends messages directly to the handlers.
	 */
	public void createStandalone(ServerRequestHandler requestHandler, ServerResponseHandler responseHandler) {
		disconnect();
		standaloneRequestHandler = Checks.isNotNull(requestHandler, "requestHandler");
		standaloneResponseHandler = Checks.isNotNull(responseHandler, "responseHandler");
		localClientId = Server.HOST_ID;
	}
	
	/**
	 * Sends a packet to the network.
	 * @param packet The packet to send
	 */
	public void send(Packet packet) {
		if (isStandalone()) {
			try {
				standaloneRequestHandler.handleRequest(new StandaloneServerRequest(packet));
			} catch (RuntimeException e) {
				getLogger().error("Failed to handle request: " + packet.getClass().getSimpleName(), e);
			}
		} else if (isConnected()) {
			session.send(packet);
		}
	}
	
	/**
	 * Synchronizes the network.
	 */
	@Override
	public void synchronize() {
		if (server != null) {
			server.synchronize();
		}
		if (session != null) {
			session.synchronize();
		}
	}
	
	public Server getServer() {
		return server;
	}
	
	public ClientSession getSession() {
		return session;
	}
	
	/**
	 * Checks whether the client is connected to a server.
	 * @return {@code true} if the client is connected to a server.
	 */
	public boolean isConnected() {
		return session != null && session.isConnected();
	}
	
	/**
	 * Checks whether the client with a given ID is the host.
	 * @param clientId The ID of the client
	 * @return {@code true} if the client is the host.
	 */
	public boolean isHost(int clientId) {
		return clientId == Server.HOST_ID;
	}
	
	public boolean isLocalClient(int clientId) {
		return clientId == localClientId && (session != null || isStandalone());
	}
	
	public boolean isStandalone() {
		return standaloneRequestHandler != null && standaloneResponseHandler != null;
	}
	
	public int getLocalClientId() {
		return localClientId;
	}
	
	public void setLocalClientId(int localClientId) {
		this.localClientId = localClientId;
	}
	
	public PacketCodec getCodec() {
		return codec;
	}
	
	public void setCodec(PacketCodec codec) {
		this.codec = codec;
	}
	
	@Override
	public void destroy() {
		disconnect();
		super.destroy();
	}
	
	/**
	 * Disconnects the client from the network.
	 */
	public void disconnect() {
		session = Destructible.destroy(session);
		server = Destructible.destroy(server);
		standaloneRequestHandler = null;
		standaloneResponseHandler = null;
	}
	
	private class StandaloneServerRequest extends ServerRequest {
		
		public StandaloneServerRequest(Packet payload) {
			super(Server.HOST_ID, payload, null, null);
		}
		
		@Override
		public void replyToAll(Packet packet) {
			acknowledge();
			reply(packet);
		}
		
		@Override
		public void reply(Packet packet) {
			acknowledge();
			if (standaloneResponseHandler != null) {
				standaloneResponseHandler.handleResponse(new StandaloneServerResponse(packet));
			}
		}
		
	}
	
	private class StandaloneServerResponse extends ServerResponse {
		
		public StandaloneServerResponse(Packet payload) {
			super(payload, null);
		}
		
		@Override
		public void reply(Packet packet) {
			acknowledge();
			if (standaloneRequestHandler != null) {
				standaloneRequestHandler.handleRequest(new StandaloneServerRequest(packet));
			}
		}
		
	}
	
}
