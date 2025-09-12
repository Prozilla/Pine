package dev.prozilla.pine.examples.flappybird.system.ground;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.component.GroundData;

/**
 * Scrolls ground elements along the horizontal axis of the screen.
 */
public class GroundMover extends UpdateSystem {
	
	public GroundMover() {
		super(GroundData.class, Transform.class);
		setApplyTimeScale(true);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getComponent(Transform.class);
		GroundData groundData = chunk.getComponent(GroundData.class);
		
		if (!groundData.gameScene.gameOver) {
			// Scroll position to the left
			transform.position.x -= deltaTime * GroundData.SPEED;
			
			// Reset position when edge is reached
			if (transform.position.x + GroundData.WIDTH < FlappyBird.WIDTH / -2f) {
				transform.position.x += FlappyBird.WIDTH + GroundData.WIDTH;
			}
		}
	}
}
