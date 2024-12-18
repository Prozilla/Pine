package dev.prozilla.pine.examples.flappybird.system.background;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

/**
 * Scrolls background elements along the horizontal axis of the screen.
 */
public class BackgroundMover extends UpdateSystem {
	
	public BackgroundMover() {
		super(BackgroundData.class, Transform.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		Transform transform = chunk.getComponent(Transform.class);
		BackgroundData backgroundData = chunk.getComponent(BackgroundData.class);
		
		if (!backgroundData.gameScene.gameOver) {
			// Scroll position to the left
			transform.x -= deltaTime * BackgroundData.SPEED;
			
			// Reset position when edge is reached
			if (transform.x + BackgroundData.WIDTH < Main.WIDTH / -2f) {
				transform.x += Main.WIDTH + BackgroundData.WIDTH;
			}
		}
	}
}
