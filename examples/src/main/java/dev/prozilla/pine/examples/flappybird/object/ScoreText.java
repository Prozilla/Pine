package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.object.canvas.Text;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class ScoreText extends Text {
	
	private GameScene gameScene;
	
	public ScoreText(Game game) {
		super(game, "0");
		
		// Set position and appearance
		setAnchor(RectTransform.Anchor.TOP_RIGHT);
		setColor(Color.WHITE);
		setOffset(16, 16);
	}
	
	@Override
	public void init(long window) throws IllegalStateException {
		super.init(window);
		
		// Store reference to scene
		gameScene = (GameScene)scene;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		// Update text field based on score value
		setText(String.valueOf(gameScene.playerScore));
	}
	
	@Override
	public String getName() {
		return "ScoreText";
	}
}
