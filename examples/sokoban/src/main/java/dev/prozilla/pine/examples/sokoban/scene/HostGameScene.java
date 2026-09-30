package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.common.asset.view.Controller;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.scene.ViewScene;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.controller.BackButtonController;

public class HostGameScene extends ViewScene {
	
	public HostGameScene() {
		super("views/host.html");
		addController(new HostController().then(new BackButtonController()));
	}
	
	@Override
	protected void load() {
		super.load();
		cameraData.setBackgroundColor(GameManager.BACKGROUND_COLOR);
	}
	
	private static class HostController implements Controller {
		
		@Override
		public void load(Node view) {
			TextNode portInput = view.getNodeById("port").getComponent(TextNode.class);
			Node hostButton = view.getNodeById("host");
			
			hostButton.onClick((event) -> {
				GameManager.instance.hostGame(Integer.parseInt(portInput.text));
			});
		}
		
	}

}
