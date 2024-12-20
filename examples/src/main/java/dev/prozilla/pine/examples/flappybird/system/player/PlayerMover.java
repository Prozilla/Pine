package dev.prozilla.pine.examples.flappybird.system.player;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

/**
 * Moves the player based on the current velocity, animates the sprite and checks if the player died.
 */
public class PlayerMover extends UpdateSystem {
	
	public PlayerMover() {
		super(Transform.class, SpriteRenderer.class, PlayerData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getComponent(Transform.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		// Check if player hit floor or ceiling
		if (transform.position.y <= Main.HEIGHT / -2f || transform.position.y + PlayerData.HEIGHT >= Main.HEIGHT / 2f) {
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
		transform.position.y += playerData.velocity * PlayerData.SPEED * deltaTime;
		playerData.velocity -= deltaTime / 2f;
		
		// Clamp position inside screen bounds
		transform.position.y = MathUtils.clamp(transform.position.y, Main.HEIGHT / -2f, Main.HEIGHT / 2f);
	}
}
