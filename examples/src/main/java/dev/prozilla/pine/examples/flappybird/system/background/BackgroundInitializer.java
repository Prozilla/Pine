package dev.prozilla.pine.examples.flappybird.system.background;

import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

/**
 * Initializes background elements.
 */
public class BackgroundInitializer extends InitSystem {
	
	public BackgroundInitializer() {
		super(BackgroundData.class, SpriteRenderer.class, Transform.class);
	}
	
	@Override
	protected void process(EntityMatch match) {
		Transform transform = match.getComponent(Transform.class);
		BackgroundData backgroundData = match.getComponent(BackgroundData.class);
		SpriteRenderer spriteRenderer = match.getComponent(SpriteRenderer.class);
		
		// Store reference to scene
		backgroundData.gameScene = (GameScene)scene;
		
		// Set sprite properties
		spriteRenderer.setRegion(0, 0, BackgroundData.WIDTH, BackgroundData.HEIGHT);
		spriteRenderer.scale = 1.0001f; // Fix lines appearing between sprites
		
		// Set initial position
		transform.setPosition(Main.WIDTH / -2f + BackgroundData.WIDTH * backgroundData.index, Main.HEIGHT / -2f);
	}
}
