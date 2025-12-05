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
		setApplyTimeScale(true);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getComponent(Transform.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		// Check if player hit floor or ceiling
		float groundY = GroundData.TOP_Y - playerData.collider.offset.y + playerData.collider.radius;
		if (transform.position.y <= groundY || transform.position.y + PlayerData.HEIGHT >= FlappyBird.HEIGHT / 2f) {
			playerData.gameScene.endGame();
		}
		
		// Crop sprite to current frame
		spriteRenderer.setRegion(playerData.animationFrame * PlayerData.SPRITE_WIDTH, 0, PlayerData.SPRITE_WIDTH, PlayerData.SPRITE_HEIGHT);
		
		if (!playerData.gameScene.gameOver) {
			// Update age and calculate frame
			playerData.age += deltaTime;
			playerData.animationFrame = Math.round((playerData.age * playerData.animationSpeed.get())) % 3;
		}
		
		// Update velocity and move based on current velocity
		playerData.velocity -= deltaTime / 2f;
		transform.position.y += playerData.velocity * playerData.speed.get() * deltaTime;
		playerData.velocity -= deltaTime / 2f;
		
//		if (Application.isDevMode()) {
//			transform.position.y = scene.getCameraData().screenToWorldPosition(application.getInput().getCursor()).y;
//		}
		
		// Clamp position inside screen bounds
		transform.position.y = MathUtils.clamp(transform.position.y, groundY, FlappyBird.HEIGHT / 2f);
		
		if (transform.position.y > groundY) {
			// Apply rotation based on velocity, unless player is dead
			float targetRotation = playerData.gameScene.gameOver ? 180 : playerData.velocity * playerData.rotationFactor.get();
			spriteRenderer.rotation = MathUtils.lerp(spriteRenderer.rotation, targetRotation, deltaTime * playerData.rotationSpeed.get());
		}
	}
}
