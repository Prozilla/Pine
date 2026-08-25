package dev.prozilla.pine.extensions.pinet.component;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.extensions.pinet.client.ClientPacketHandler;
import dev.prozilla.pine.extensions.pinet.client.ClientSession;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
import dev.prozilla.pine.extensions.pinet.server.Server;
import dev.prozilla.pine.extensions.pinet.server.ServerRequestHandler;

import java.io.IOException;

/**
 * Manages a client's connection to a network.
 * <p>
 * The network can either be a {@link Server}, in which case the connection is local if the client is the host and otherwise remote,
 * or a standalone network, in which case the "client" has no connection and {@link Packet}s get sent directly to a {@link ServerRequestHandler}.
 */
public class NetworkManager extends Component {
	
	private Server server;
	private ClientSession session;
	private int localClientId;
	private ServerRequestHandler localRequestHandler;
	private PacketCodec codec;
	
	public NetworkManager() {
		this(new PacketCodec());
	}
	
	public NetworkManager(PacketCodec codec) {
		this.codec = codec;
		localClientId = -1;
	}
	
	/**
	 * Creates an integrated server and a local connection to it.
	 */
	public void createHost(int port, ServerRequestHandler requestHandler, ClientPacketHandler packetHandler) {
		disconnect();
		try {
			server = new Server(port, requestHandler, codec);
			session = ClientSession.createLocal(server.connectHost(), packetHandler);
		} catch (IOException e) {
			getLogger().error("Failed to start server", e);
		}
	}
	
	/**
	 * Creates a connection to a remote server.
	 * @param address The address of the server
	 */
	public void createClient(String address, int port, ClientPacketHandler packetHandler) {
		disconnect();
		try {
			session = ClientSession.createRemote(address, port, packetHandler, codec);
		} catch (IOException e) {
			getLogger().error("Failed to connect to server", e);
		}
	}
	
	/**
	 * Creates a standalone network that sends packets directly to a request handler.
	 * @param requestHandler The request handler
	 */
	public void createStandalone(ServerRequestHandler requestHandler) {
		disconnect();
		localRequestHandler = requestHandler;
		localClientId = Server.HOST_ID;
	}
	
	/**
	 * Sends a packet to the network.
	 * @param packet The packet to send
	 */
	public void send(Packet packet) {
		if (localRequestHandler != null) {
			localRequestHandler.handleRequest(new Server.Request(packet));
		} else if (session != null) {
			session.send(packet);
		}
	}
	
	/**
	 * Synchronizes the network.
	 */
	public void synchronize() {
		if (server != null) {
			server.synchronize();
		}
		if (session != null) {
			session.synchronize();
		}
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
		return clientId == localClientId && (session != null || localRequestHandler != null);
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
		localRequestHandler = null;
	}
	
}
