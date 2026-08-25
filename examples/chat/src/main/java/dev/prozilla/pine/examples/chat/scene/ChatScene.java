package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.examples.chat.EntityTag;
import dev.prozilla.pine.examples.chat.entity.ChatPrefab;
import dev.prozilla.pine.examples.chat.request.RequestHandler;
import dev.prozilla.pine.examples.chat.request.SendMessagePacket;
import dev.prozilla.pine.examples.chat.request.SetUsernamePacket;
import dev.prozilla.pine.examples.chat.response.*;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.entity.NetworkManagerPrefab;
import dev.prozilla.pine.extensions.pinet.message.ServerMessageFilter;
import dev.prozilla.pine.extensions.pinet.message.ServerMessageLogger;
import dev.prozilla.pine.extensions.pinet.message.ServerMessagePipeline;
import dev.prozilla.pine.extensions.pinet.system.NetworkSynchronizer;

public class ChatScene extends SceneBase {
	
	private final String username;
	private NetworkManager network;
	private ServerMessagePipeline messagePipeline;
	
	public ChatScene(String username) {
		this.username = username;
	}
	
	@Override
	protected void load() {
		super.load();
		
		// Set up network
		network = addEntity(new NetworkManagerPrefab()).getComponent(NetworkManager.class);
		addSystem(new NetworkSynchronizer());
		network.getCodec()
			.addDecoder(ClientJoinPacket.ID, ClientJoinPacket::decode)
			.addDecoder(ClientLeavePacket.ID, ClientLeavePacket::decode)
			.addDecoder(MessagePacket.ID, MessagePacket::decode)
			.addDecoder(SendMessagePacket.ID, SendMessagePacket::decode)
			.addDecoder(SetUsernamePacket.ID, SetUsernamePacket::decode)
			.addDecoder(WelcomePacket.ID, WelcomePacket::decode);
		
		// Create chat UI
		Entity chat = nodeRoot.addChild(new ChatPrefab(network, font));
		Entity messageList = chat.getChildWithTag(EntityTag.MESSAGE_LIST);
		
		// Create message pipeline
		messagePipeline = new ResponseHandler(network, username, messageList, font).and(new RequestHandler())
			.then(ServerMessageFilter.unacknowledged())
			.then(new ServerMessageLogger(logger));
	}
	
	public void startHost(int port) {
		network.createHost(port, messagePipeline);
	}
	
	public void startClient(String address, int port) {
		network.createClient(address, port, messagePipeline);
	}
	
	@Override
	public void destroy() throws IllegalStateException {
		network = Destructible.destroy(network);
		super.destroy();
	}
}
