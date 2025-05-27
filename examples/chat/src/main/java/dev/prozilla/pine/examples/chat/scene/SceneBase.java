package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.Chat;

public abstract class SceneBase extends Scene {
	
	protected Chat chatApp;
	protected Entity nodeRoot;
	
	@Override
	public void setApplication(Application application) {
		super.setApplication(application);
		chatApp = (Chat)application;
	}
	
	@Override
	protected void load() {
		super.load();
		
		nodeRoot = world.addEntity(new NodeRootPrefab());
	}
}
