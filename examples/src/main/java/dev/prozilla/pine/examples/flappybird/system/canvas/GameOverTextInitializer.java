package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.GameOverTextData;

public class GameOverTextInitializer extends InitSystem {
	
	public GameOverTextInitializer() {
		super(GameOverTextData.class, TextRenderer.class);
	}
	
	@Override
	public void init(long window) {
		forEach(match -> {
			Entity entity = match.getEntity();
			GameOverTextData gameOverTextData = match.getComponent(GameOverTextData.class);
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			// Set font
			gameOverTextData.gameScene = (GameScene)scene;
			textRenderer.setFont(gameOverTextData.gameScene.font);
			
			// Hide text at the beginning
			entity.setActive(false);
		});
	}
}
