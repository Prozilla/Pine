package dev.prozilla.pine.examples.chat.packet;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;
import dev.prozilla.pine.examples.chat.EntityTag;
import dev.prozilla.pine.examples.chat.request.SetUsernameRequest;
import dev.prozilla.pine.extensions.pinet.client.ClientPacketHandler;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.packet.Packet;

public class PacketHandler extends UpdateSystemBase implements ClientPacketHandler {
	
	private final NetworkManager network;
	private final String username;
	private final TextPrefab messagePrefab;
	
	public PacketHandler(NetworkManager network, String username, Font font) {
		super(Transform.class);
		setRequiredTag(EntityTag.MESSAGE_LIST);
		
		this.network = network;
		this.username = username;
		
		messagePrefab = new TextPrefab();
		messagePrefab.setFont(font);
	}
	
	@Override
	public void update(float deltaTime) {
	
	}
	
	@Override
	public void handlePacket(Packet packet) {
		if (packet instanceof WelcomePacket(int clientId)) {
			network.setLocalClientId(clientId);
			network.send(new SetUsernameRequest(clientId, username));
		} else if (packet instanceof MessagePacket(int ignored, String name, String content)) {
			addMessage(String.format("%s: %s", name, content), Color.white());
		} else if (packet instanceof ClientJoinPacket(int ignored, String name)) {
			addMessage(String.format("%s has joined", name), Color.gray());
		} else if (packet instanceof ClientLeavePacket(int ignored, String name)) {
			addMessage(String.format("%s has left", name), Color.gray());
		}
	}
	
	private void addMessage(String message, Color color) {
		Entity messageList = getChunks().getFirst().getEntity();
		messagePrefab.setColor(color);
		messagePrefab.setText(message);
		messageList.addChild(messagePrefab);
	}
	
}
