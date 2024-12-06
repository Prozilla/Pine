package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PipeData;

/**
 * Initializes pipes.
 */
public class PipeInitializer extends InitSystem {
	
	public PipeInitializer() {
		super(PipeData.class, SpriteRenderer.class, Transform.class);
	}
	
	@Override
	protected void process(EntityMatch match) {
		PipeData pipeData = match.getComponent(PipeData.class);
		SpriteRenderer spriteRenderer = match.getComponent(SpriteRenderer.class);
		Transform transform = match.getComponent(Transform.class);
		
		// Set sprite properties
		spriteRenderer.setRegion(0, 0, PipeData.WIDTH, PipeData.HEIGHT);
		spriteRenderer.scale = 1.25f;
		if (pipeData.isTop) {
			// Flip sprite
			spriteRenderer.rotation = -180;
		}
		
		// Set initial position
		transform.x = Main.WIDTH / 2f;
	}
}
