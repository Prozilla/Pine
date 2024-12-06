package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class GameOverTextInitializer extends InitSystem {
	
	public GameOverTextInitializer() {
		super(TextRenderer.class);
		requireTag(EntityTag.GAME_OVER_TAG);
	}
	
	@Override
	public void init(long window) {
		GameScene gameScene = (GameScene)scene;
		
		forEach(match -> {
			Entity entity = match.getEntity();
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			// Set font
			textRenderer.setFont(gameScene.font);
			
			// Hide text at the beginning
			entity.setActive(false);
		});
	}
}
