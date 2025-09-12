package dev.prozilla.pine.examples.flappybird.system.ground;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.component.GroundData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

/**
 * Initializes ground elements.
 */
public class GroundInitializer extends InitSystem {
	
	public GroundInitializer() {
		super(GroundData.class, SpriteRenderer.class, Transform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Transform transform = chunk.getComponent(Transform.class);
		GroundData groundData = chunk.getComponent(GroundData.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		
		// Store reference to scene
		if (scene instanceof GameScene gameScene) {
			groundData.gameScene = gameScene;
		}
		
		// Set sprite properties
		spriteRenderer.setRegion(0, 0, GroundData.WIDTH, GroundData.HEIGHT);
		
		// Set initial position
		transform.setPosition(FlappyBird.WIDTH / -2f + GroundData.WIDTH * groundData.index, FlappyBird.HEIGHT / -2f + GroundData.ELEVATION);
	}
}
