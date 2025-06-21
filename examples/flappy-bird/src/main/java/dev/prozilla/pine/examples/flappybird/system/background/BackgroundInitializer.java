package dev.prozilla.pine.examples.flappybird.system.background;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

/**
 * Initializes background elements.
 */
public class BackgroundInitializer extends InitSystem {
	
	public BackgroundInitializer() {
		super(BackgroundData.class, SpriteRenderer.class, Transform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Transform transform = chunk.getComponent(Transform.class);
		BackgroundData backgroundData = chunk.getComponent(BackgroundData.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		
		// Store reference to scene
		if (scene instanceof GameScene gameScene) {
			backgroundData.gameScene = gameScene;
		}
		
		// Set sprite properties
		spriteRenderer.setRegion(0, 0, BackgroundData.WIDTH, BackgroundData.HEIGHT);
		spriteRenderer.scale = 1.0001f; // Fix lines appearing between sprites
		
		// Set initial position
		transform.setPosition(FlappyBird.WIDTH / -2f + BackgroundData.WIDTH * backgroundData.index, FlappyBird.HEIGHT / -2f);
	}
}
