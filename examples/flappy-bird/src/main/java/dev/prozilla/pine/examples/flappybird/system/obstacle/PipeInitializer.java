package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.mesh.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.GameManager;
import dev.prozilla.pine.examples.flappybird.component.PipeData;

/**
 * Initializes pipes.
 */
public class PipeInitializer extends InitSystem {
	
	public PipeInitializer() {
		super(PipeData.class, SpriteRenderer.class, Transform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		PipeData pipeData = chunk.getComponent(PipeData.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		Transform transform = chunk.getComponent(Transform.class);
		
		// Set sprite properties
		spriteRenderer.getMesh().setRegion(PipeData.SPRITE_WIDTH * GameManager.instance.pipeVariant, 0, PipeData.SPRITE_WIDTH, PipeData.SPRITE_HEIGHT);
		transform.scale.set(PipeData.SCALE);
		if (pipeData.isTop) {
			// Flip sprite
			transform.rotation.z = 180;
		}
		
		// Set initial position
		transform.position.x = FlappyBird.WIDTH / 2f;
	}
}
