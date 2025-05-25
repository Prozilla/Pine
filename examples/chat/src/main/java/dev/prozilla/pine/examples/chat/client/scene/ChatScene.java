package dev.prozilla.pine.examples.chat.client.scene;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.client.Client;

public class ChatScene extends Scene {
	
	private final Client client;
	
	public ChatScene(Client client) {
		this.client = client;
	}
	
	@Override
	protected void load() {
		super.load();
		
		Entity nodeRoot = world.addEntity(new NodeRootPrefab());
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		Entity layoutNode = nodeRoot.addChild(layoutPrefab);
	}
}
