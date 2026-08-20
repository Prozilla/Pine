package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class HostGameScene extends OnlineMenuScene {

	@Override
	protected void load() {
		super.load();

		TextNode portNode = addPortInput(GameManager.DEFAULT_PORT);

		addButton("Start", (button) -> {
			GameManager.instance.hostGame(Integer.parseInt(portNode.text));
		});
	}

}
