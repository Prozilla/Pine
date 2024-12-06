package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.ScoreTextData;

public class ScoreTextInitializer extends InitSystem {
	
	public ScoreTextInitializer() {
		super(ScoreTextData.class, TextRenderer.class);
	}
	
	@Override
	public void init(long window) {
		forEach(match -> {
			ScoreTextData scoreTextData = match.getComponent(ScoreTextData.class);
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			// Store reference to scene
			scoreTextData.gameScene = (GameScene)scene;
			
			// Set font
			textRenderer.setFont(scoreTextData.gameScene.font);
		});
	}
}
