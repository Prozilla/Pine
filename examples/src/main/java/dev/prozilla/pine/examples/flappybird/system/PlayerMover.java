package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
import dev.prozilla.pine.examples.flappybird.entity.Player;

public class PlayerMover extends UpdateSystem {
	
	public PlayerMover() {
		super(Player.collector);
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(componentGroup -> {
			Transform transform = componentGroup.getComponent(Transform.class);
			SpriteRenderer spriteRenderer = componentGroup.getComponent(SpriteRenderer.class);
			PlayerData playerData = componentGroup.getComponent(PlayerData.class);
			
			if (transform.x <= Main.HEIGHT / -2f || transform.y + PlayerData.HEIGHT >= Main.HEIGHT / 2f) {
				playerData.gameScene.endGame();
			}
			
			// Crop sprite to current frame
			spriteRenderer.setRegion(playerData.animationFrame * PlayerData.WIDTH, 0, PlayerData.WIDTH, PlayerData.HEIGHT);
			
			if (!playerData.gameScene.gameOver) {
				// Update age and calculate frame
				playerData.age += deltaTime;
				playerData.animationFrame = Math.round((playerData.age * PlayerData.ANIMATION_SPEED)) % 3;
			} else {
				// Flip sprite if player is dead
				spriteRenderer.rotation = 180;
			}
			
			// Update velocity and move based on current velocity
			playerData.velocity -= deltaTime / 2f;
			transform.y += playerData.velocity * PlayerData.SPEED * deltaTime;
			playerData.velocity -= deltaTime / 2f;
			
			// Clamp position inside screen bounds
			transform.y = MathUtils.clamp(transform.y, Main.HEIGHT / -2f, Main.HEIGHT / 2f);
		});
	}
}
