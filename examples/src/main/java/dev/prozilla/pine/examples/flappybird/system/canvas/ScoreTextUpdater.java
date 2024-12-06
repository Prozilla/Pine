package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.system.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class ScoreTextUpdater extends UpdateSystem {
	
	public ScoreTextUpdater() {
		super(TextRenderer.class);
		requireTag(EntityTag.SCORE_TAG);
	}
	
	@Override
	public void update(float deltaTime) {
		GameScene gameScene = (GameScene)scene;
		
		forEach(match -> {
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			// Update text field based on score value
			textRenderer.setText(String.valueOf(gameScene.playerScore));
		});
	}
}
