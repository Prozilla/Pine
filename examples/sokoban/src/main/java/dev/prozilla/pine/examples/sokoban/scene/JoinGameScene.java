package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.common.asset.view.Controller;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.scene.ViewScene;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.controller.BackButtonController;

public class JoinGameScene extends ViewScene {
	
	public JoinGameScene() {
		super("views/join.html");
		addController(new JoinController().then(new BackButtonController()));
	}
	
	@Override
	protected void load() {
		super.load();
		cameraData.setBackgroundColor(GameManager.BACKGROUND_COLOR);
	}
	
	private static class JoinController implements Controller {
		
		@Override
		public void load(Node view) {
			TextNode hostInput = view.getNodeById("host").getComponent(TextNode.class);
			TextNode portInput = view.getNodeById("port").getComponent(TextNode.class);
			Node joinButton = view.getNodeById("join");
			
			joinButton.onClick((event) -> {
				GameManager.instance.joinGame(hostInput.text, Integer.parseInt(portInput.text));
			});
		}
		
	}

}
