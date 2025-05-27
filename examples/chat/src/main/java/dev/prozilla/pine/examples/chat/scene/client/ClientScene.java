package dev.prozilla.pine.examples.chat.scene.client;

import dev.prozilla.pine.examples.chat.entity.ChatPrefab;
import dev.prozilla.pine.examples.chat.net.user.Client;
import dev.prozilla.pine.examples.chat.scene.SceneBase;

public class ClientScene extends SceneBase {
	
	private final Client client;
	
	public ClientScene(Client client) {
		this.client = client;
	}
	
	@Override
	protected void load() {
		super.load();
		
		nodeRoot.addChild(new ChatPrefab(client, font));
	}
}
