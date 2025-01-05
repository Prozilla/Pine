package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.snake.EntityTag;
import dev.prozilla.pine.examples.snake.GameScene;
import dev.prozilla.pine.examples.snake.component.PlayerData;

import java.awt.*;

public class PlayerHeadMover extends UpdateSystem {
	
	public PlayerHeadMover() {
		super(PlayerData.class, TileRenderer.class, SpriteRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		TileRenderer tile = chunk.getComponent(TileRenderer.class);
		SpriteRenderer sprite = chunk.getComponent(SpriteRenderer.class);
		
		// Check if it's time to move
		playerData.timeUntilNextMove -= deltaTime;
		if (playerData.timeUntilNextMove <= 0) {
			Point nextCoordinate = new Point(tile.coordinate.x, tile.coordinate.y);
			
			switch (playerData.direction) {
				case 0 -> nextCoordinate.y += 1;
				case 1 -> nextCoordinate.x -= 1;
				case 2 -> nextCoordinate.y -= 1;
				case 3 -> nextCoordinate.x += 1;
			}
			
			TileRenderer nextTile = tile.getGroup().getTile(nextCoordinate);
			if (nextTile != null) {
				if (nextTile.getEntity().hasTag(EntityTag.APPLE_TAG)) {
					// Eat apple
					nextTile.remove();
					System.out.println("Apple eaten");
				}
			}
			
			// Move player
			tile.moveTo(nextCoordinate);
			
			// Change sprite based on current direction
			int spriteRegionY = GameScene.CELL_SIZE * (3 - playerData.direction);
			sprite.setRegion(0, spriteRegionY, GameScene.CELL_SIZE, GameScene.CELL_SIZE);
			
			// Reset timer
			playerData.timeUntilNextMove += PlayerData.TIME_BETWEEN_MOVES;
			
			playerData.previousMoveDirection = playerData.direction;
		}
	}
}
