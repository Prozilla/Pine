package dev.prozilla.pine.examples.flappybird.controller;

import dev.prozilla.pine.common.asset.view.Controller;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class GameOverController implements Controller {
	
	private final GameScene gameScene;
	
	public GameOverController(GameScene gameScene) {
		this.gameScene = gameScene;
	}
	
	@Override
	public void load(Node view) {
		view.getNodeById("final-score").getComponent(TextNode.class).setText(String.format("Your Score: %s", gameScene.playerScore));
		view.getNodeById("restart").onClick((event) -> gameScene.reload());
		view.getNodeById("back").onClick((event) -> gameScene.getApplication().nextScene());
	}
	
}
