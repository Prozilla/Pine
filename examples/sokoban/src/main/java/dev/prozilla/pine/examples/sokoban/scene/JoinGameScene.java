package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class JoinGameScene extends OnlineMenuScene {

	@Override
	protected void load() {
		super.load();

		TextNode hostNode = addTextInput(GameManager.DEFAULT_HOST);
		TextNode portNode = addPortInput(GameManager.DEFAULT_PORT);

		addButton("Connect", (button) -> {
			GameManager.instance.joinGame(hostNode.text, Integer.parseInt(portNode.text));
		});
	}

}
