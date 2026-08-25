package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.examples.chat.entity.ChatPrefab;
import dev.prozilla.pine.examples.chat.packet.*;
import dev.prozilla.pine.examples.chat.request.RequestHandler;
import dev.prozilla.pine.examples.chat.request.SendMessageRequest;
import dev.prozilla.pine.examples.chat.request.SetUsernameRequest;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.entity.NetworkManagerPrefab;
import dev.prozilla.pine.extensions.pinet.system.NetworkSynchronizer;

public class ChatScene extends SceneBase {
	
	private final String username;
	private NetworkManager network;
	private PacketHandler packetHandler;
	
	public ChatScene(String username) {
		this.username = username;
	}
	
	@Override
	protected void load() {
		super.load();
		
		// Set up network
		network = addEntity(new NetworkManagerPrefab()).getComponent(NetworkManager.class);
		addSystem(new NetworkSynchronizer());
		packetHandler = addSystem(new PacketHandler(network, username, font));
		network.getCodec()
			.addDecoder(ClientJoinPacket.ID, ClientJoinPacket::decode)
			.addDecoder(ClientLeavePacket.ID, ClientLeavePacket::decode)
			.addDecoder(MessagePacket.ID, MessagePacket::decode)
			.addDecoder(SendMessageRequest.ID, SendMessageRequest::decode)
			.addDecoder(SetUsernameRequest.ID, SetUsernameRequest::decode)
			.addDecoder(WelcomePacket.ID, WelcomePacket::decode);
		
		nodeRoot.addChild(new ChatPrefab(network, font));
	}
	
	public void startHost(int port) {
		network.createHost(port, new RequestHandler(), packetHandler);
	}
	
	public void startClient(String address, int port) {
		network.createClient(address, port, packetHandler);
	}
	
	@Override
	public void destroy() throws IllegalStateException {
		network = Destructible.destroy(network);
		super.destroy();
	}
}
