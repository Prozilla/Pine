package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

public class BackgroundMover extends UpdateSystem {
	
	public BackgroundMover() {
		super(new EntityQuery(BackgroundData.class, Transform.class));
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
			Transform transform = match.getComponent(Transform.class);
			BackgroundData backgroundData = match.getComponent(BackgroundData.class);
			
			if (!backgroundData.gameScene.gameOver) {
				// Scroll position to the left
				transform.x -= deltaTime * BackgroundData.SPEED;
				
				// Reset position when edge is reached
				if (transform.x + BackgroundData.WIDTH < Main.WIDTH / -2f) {
					transform.x += Main.WIDTH + BackgroundData.WIDTH;
				}
			}
		});
	}
}
