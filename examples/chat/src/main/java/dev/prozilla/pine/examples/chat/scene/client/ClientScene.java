package dev.prozilla.pine.examples.chat.scene.client;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.entity.ChatPrefab;
import dev.prozilla.pine.examples.chat.net.user.Client;

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
