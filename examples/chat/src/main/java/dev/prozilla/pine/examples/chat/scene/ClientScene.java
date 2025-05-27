package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.client.Client;
import dev.prozilla.pine.examples.chat.entity.ChatPrefab;

public class ClientScene extends Scene {
	
	private final Client client;
	
	public ClientScene(Client client) {
		this.client = client;
	}
	
	@Override
	protected void load() {
		super.load();
		
		Entity nodeRoot = world.addEntity(new NodeRootPrefab());
		nodeRoot.addChild(new ChatPrefab(client));
	}
}
