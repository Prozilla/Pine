package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.ui.TextRenderer;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.GameScene;

/**
 * Updates the score text based on the player's score.
 */
public class ScoreTextUpdater extends UpdateSystemBase {
	
	public ScoreTextUpdater() {
		super(TextRenderer.class);
		setRequiredTag(EntityTag.SCORE_TAG);
	}
	
	@Override
	public void update(float deltaTime) {
		GameScene gameScene = (GameScene)scene;
		
		forEach(chunk -> {
			TextRenderer textRenderer = chunk.getComponent(TextRenderer.class);
			
			// Update text field based on score value
			textRenderer.setText(String.valueOf(gameScene.playerScore));
		});
	}
}
