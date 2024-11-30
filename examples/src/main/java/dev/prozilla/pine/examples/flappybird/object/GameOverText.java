package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.object.canvas.Text;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class GameOverText extends Text {
	
	public GameOverText(Game game) {
		super(game, "Game Over");
		
		// Set position and appearance
		setAnchor(RectTransform.Anchor.CENTER);
		setColor(Color.WHITE);
	}
	
	@Override
	public void init(long window) throws IllegalStateException {
		super.init(window);
		
		// Set font
		GameScene gameScene = (GameScene)scene;
		setFont(gameScene.font);
		
		// Hide text at the beginning
		setActive(false);
	}
}
