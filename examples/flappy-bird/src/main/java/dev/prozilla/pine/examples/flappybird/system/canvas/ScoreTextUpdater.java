package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

/**
 * Updates the score text based on the player's score.
 */
public class ScoreTextUpdater extends UpdateSystemBase {
	
	public ScoreTextUpdater() {
		super(TextNode.class);
		setRequiredTag(EntityTag.SCORE_TAG);
	}
	
	@Override
	public void update(float deltaTime) {
		GameScene gameScene = (GameScene)scene;
		
		forEach(chunk -> {
			TextNode textRenderer = chunk.getComponent(TextNode.class);
			
			// Update text field based on score value
			textRenderer.setText(String.valueOf(gameScene.playerScore));
		});
	}
}
