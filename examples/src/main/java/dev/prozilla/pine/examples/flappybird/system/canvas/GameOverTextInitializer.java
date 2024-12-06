package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.system.init.InitSystemBase;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.GameScene;

/**
 * Initializes the game over text.
 */
public class GameOverTextInitializer extends InitSystemBase {
	
	public GameOverTextInitializer() {
		super(TextRenderer.class);
		requireTag(EntityTag.GAME_OVER_TAG);
	}
	
	@Override
	public void init() {
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
