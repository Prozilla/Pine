package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.system.init.InitSystemBase;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.GameScene;

/**
 * Initializes the score text.
 */
public class ScoreTextInitializer extends InitSystemBase {
	
	public ScoreTextInitializer() {
		super(TextRenderer.class);
		requireTag(EntityTag.SCORE_TAG);
	}
	
	@Override
	public void init() {
		GameScene gameScene = (GameScene)scene;
		
		forEach(chunk -> {
			TextRenderer textRenderer = chunk.getComponent(TextRenderer.class);
			
			// Set font
			textRenderer.setFont(gameScene.font);
		});
	}
}
