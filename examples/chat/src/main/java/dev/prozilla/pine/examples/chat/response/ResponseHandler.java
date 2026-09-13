package dev.prozilla.pine.examples.chat.response;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.chat.request.SetUsernamePacket;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponseHandler;

public class ResponseHandler implements ServerResponseHandler {
	
	private final NetworkManager network;
	private final String username;
	private final Entity messageList;
	private final TextPrefab messagePrefab;
	
	public ResponseHandler(NetworkManager network, String username, Entity messageList, TextPrefab messagePrefab) {
		this.network = network;
		this.username = username;
		this.messageList = messageList;
		this.messagePrefab = messagePrefab;
	}
	
	@Override
	public void handleResponse(ServerResponse response) {
		switch (response.getPayload()) {
			case WelcomePacket(int clientId) -> {
				network.setLocalClientId(clientId);
				response.reply(new SetUsernamePacket(username));
			}
			case MessagePacket(String name, String content) -> {
				response.acknowledge();
				addMessage(String.format("%s: %s", name, content), Color.white());
			}
			case ClientJoinPacket(String name) -> {
				response.acknowledge();
				addMessage(String.format("%s has joined", name), Color.gray());
			}
			case ClientLeavePacket(String name) -> {
				response.acknowledge();
				addMessage(String.format("%s has left", name), Color.gray());
			}
			default -> {}
		}
	}
	
	@Override
	public void handleDisconnect(ServerResponse response) {
		response.acknowledge();
		network.getApplication().loadScene(0);
	}
	
	private void addMessage(String message, Color color) {
		if (messageList == null) {
			return;
		}
		
		messagePrefab.setColor(color);
		messagePrefab.setText(message);
		messageList.addChild(messagePrefab);
	}
	
}
