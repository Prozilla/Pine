package dev.prozilla.pine.examples.flappybird.system.player;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class PlayerMover extends UpdateSystem {
	
	public PlayerMover() {
		super(Transform.class, SpriteRenderer.class, PlayerData.class);
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
			Transform transform = match.getComponent(Transform.class);
			SpriteRenderer spriteRenderer = match.getComponent(SpriteRenderer.class);
			PlayerData playerData = match.getComponent(PlayerData.class);
			
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
