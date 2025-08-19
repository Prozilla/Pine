package dev.prozilla.pine.examples.flappybird.system.player;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.component.GroundData;
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
		if (transform.position.y <= GroundData.TOP_Y || transform.position.y + PlayerData.SPRITE_HEIGHT >= FlappyBird.HEIGHT / 2f) {
			playerData.gameScene.endGame();
		}
		
		// Crop sprite to current frame
		spriteRenderer.setRegion(playerData.animationFrame * PlayerData.SPRITE_WIDTH, 0, PlayerData.SPRITE_WIDTH, PlayerData.SPRITE_HEIGHT);
		
		if (!playerData.gameScene.gameOver) {
			// Update age and calculate frame
			playerData.age += deltaTime;
			playerData.animationFrame = Math.round((playerData.age * PlayerData.ANIMATION_SPEED)) % 3;
		}
		
		// Update velocity and move based on current velocity
		playerData.velocity -= deltaTime / 2f;
		transform.position.y += playerData.velocity * PlayerData.SPEED * deltaTime;
		playerData.velocity -= deltaTime / 2f;
		
		// Clamp position inside screen bounds
		transform.position.y = MathUtils.clamp(transform.position.y, GroundData.TOP_Y, FlappyBird.HEIGHT / 2f);
		
		if (transform.position.y > GroundData.TOP_Y) {
			// Apply rotation based on velocity, unless player is dead
			float targetRotation = playerData.gameScene.gameOver ? 180 : playerData.velocity * PlayerData.ROTATION_FACTOR;
			spriteRenderer.rotation = MathUtils.lerp(spriteRenderer.rotation, targetRotation, deltaTime * PlayerData.ROTATION_SPEED);
		}
	}
}
