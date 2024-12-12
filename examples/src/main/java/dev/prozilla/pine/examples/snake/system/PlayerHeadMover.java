package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.snake.GameScene;
import dev.prozilla.pine.examples.snake.component.PlayerData;

public class PlayerHeadMover extends UpdateSystem {
	
	public PlayerHeadMover() {
		super(PlayerData.class, SpriteRenderer.class, Transform.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		PlayerData playerData = match.getComponent(PlayerData.class);
		Transform transform = match.getComponent(Transform.class);
		SpriteRenderer sprite = match.getComponent(SpriteRenderer.class);
		
		// Check if it's time to move
		playerData.timeUntilNextMove -= deltaTime;
		if (playerData.timeUntilNextMove <= 0) {
			// Move player
			switch (playerData.direction) {
				case 0 -> transform.y += GameScene.CELL_SIZE;
				case 1 -> transform.x -= GameScene.CELL_SIZE;
				case 2 -> transform.y -= GameScene.CELL_SIZE;
				case 3 -> transform.x += GameScene.CELL_SIZE;
			}
			
			// Change sprite based on current direction
			int spriteRegionY = GameScene.CELL_SIZE * (3 - playerData.direction);
			sprite.setRegion(0, spriteRegionY, GameScene.CELL_SIZE, GameScene.CELL_SIZE);
			
			// Reset timer
			playerData.timeUntilNextMove = PlayerData.TIME_BETWEEN_MOVES;
		}
	}
}
