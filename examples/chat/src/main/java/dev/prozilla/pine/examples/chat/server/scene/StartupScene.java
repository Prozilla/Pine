package dev.prozilla.pine.examples.chat.server.scene;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.server.Server;
import dev.prozilla.pine.examples.chat.server.ServerApp;

public class StartupScene extends Scene {
	
	private ServerApp serverApp;
	
	@Override
	public void setApplication(Application application) {
		super.setApplication(application);
		serverApp = (ServerApp)application;
	}
	
	@Override
	protected void load() {
		super.load();
		
		Entity nodeRoot = world.addEntity(new NodeRootPrefab());
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		Entity layoutNode = nodeRoot.addChild(layoutPrefab);
		
		TextButtonPrefab buttonPrefab = new TextButtonPrefab("Start");
		buttonPrefab.setClickCallback((button) -> {
			serverApp.startServer(Server.DEFAULT_PORT);
		});
		layoutNode.addChild(buttonPrefab);
	}
	
}
