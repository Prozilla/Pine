package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.sokoban.net.client.ClientSession;
import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.server.Server;
import dev.prozilla.pine.examples.sokoban.system.RequestProcessor;

import java.io.IOException;
import java.util.function.Consumer;

public class NetworkManager extends Component {
	
	private Server server;
	private ClientSession session;
	private Consumer<Packet> inbox;
	private int localPlayerId = -1;
	
	public void startHost(Server server) {
		this.server = server;
		session = ClientSession.createLocal(server.connectHost());
	}
	
	public void startClient(String address, int port) {
		try {
			session = ClientSession.createRemote(address, port);
		} catch (IOException e) {
			getLogger().error("Failed to connect to host", e);
		}
	}
	
	public void startLocal(Consumer<Packet> inbox) {
		this.inbox = inbox;
	}
	
	public void send(Packet packet) {
		if (inbox != null) {
			inbox.accept(packet);
		} else if (session != null) {
			session.send(packet);
		}
	}
	
	public void tick(Consumer<Packet> consumer) {
		if (server != null) {
			server.tick();
		}
		if (session != null) {
			session.tick(consumer);
		}
	}
	
	public ClientSession getSession() {
		return session;
	}
	
	public boolean isConnected() {
		return session != null && session.isConnected();
	}
	
	public boolean isHost(int playerId) {
		return playerId == RequestProcessor.HOST_PLAYER_ID;
	}
	
	public boolean isLocalPlayer(int playerId) {
		return (session != null || inbox != null) && playerId == getLocalPlayerId();
	}
	
	public void setLocalPlayerId(int localPlayerId) {
		this.localPlayerId = localPlayerId;
	}
	
	public int getLocalPlayerId() {
		return localPlayerId;
	}
	
	@Override
	public void destroy() {
		session = Destructible.destroy(session);
		server = Destructible.destroy(server);
		inbox = null;
		
		super.destroy();
	}
	
}
