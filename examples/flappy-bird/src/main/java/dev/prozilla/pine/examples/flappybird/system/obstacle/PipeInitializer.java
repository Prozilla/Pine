package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
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
		spriteRenderer.setRegion(0, 0, PipeData.SPRITE_WIDTH, PipeData.SPRITE_HEIGHT);
		spriteRenderer.scale.set(PipeData.SCALE);
		if (pipeData.isTop) {
			// Flip sprite
			spriteRenderer.rotation = 180;
		}
		
		// Set initial position
		transform.position.x = FlappyBird.WIDTH / 2f;
	}
}
