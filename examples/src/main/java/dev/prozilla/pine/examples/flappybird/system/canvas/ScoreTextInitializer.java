package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class ScoreTextInitializer extends InitSystem {
	
	public ScoreTextInitializer() {
		super(TextRenderer.class);
		requireTag(EntityTag.SCORE_TAG);
	}
	
	@Override
	public void init(long window) {
		GameScene gameScene = (GameScene)scene;
		
		forEach(match -> {
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			// Set font
			textRenderer.setFont(gameScene.font);
		});
	}
}
