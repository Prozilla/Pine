package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.prefab.ui.Controller;
import dev.prozilla.pine.core.scene.ViewScene;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class MenuScene extends ViewScene {
	
	public MenuScene() {
		super("views/menu.html");
		addController(new MenuController());
	}
	
	@Override
	protected void load() {
		super.load();
		cameraData.setBackgroundColor(GameManager.BACKGROUND_COLOR);
	}
	
	private class MenuController implements Controller {
		
		@Override
		public void load(Node view) {
			Node singleplayerButton = view.getNodeById("singleplayer");
			Node hostButton = view.getNodeById("host");
			Node joinButton = view.getNodeById("join");
			Node quitButton = view.getNodeById("quit");
			
			singleplayerButton.onClick((button) -> getApplication().loadScene(new GameScene()));
			hostButton.onClick((button) -> getApplication().loadScene(new HostGameScene()));
			joinButton.onClick((button) -> getApplication().loadScene(new JoinGameScene()));
			quitButton.onClick((button) -> getApplication().stop());
		}
		
	}

}
