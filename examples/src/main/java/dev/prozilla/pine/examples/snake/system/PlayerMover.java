package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.snake.EntityTag;
import dev.prozilla.pine.examples.snake.GameScene;
import dev.prozilla.pine.examples.snake.component.AppleData;
import dev.prozilla.pine.examples.snake.component.PlayerData;
import dev.prozilla.pine.examples.snake.component.PlayerTailData;
import dev.prozilla.pine.examples.snake.entity.PlayerTailPrefab;

import static dev.prozilla.pine.examples.snake.GameScene.*;

public class PlayerMover extends UpdateSystem {
	
	public PlayerMover() {
		super(PlayerData.class, TileRenderer.class, SpriteRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		TileRenderer headTile = chunk.getComponent(TileRenderer.class);
		SpriteRenderer sprite = chunk.getComponent(SpriteRenderer.class);
		
		if (playerData.scene.gameOver) {
			return;
		}
		
		// Check if it's time to move
		playerData.timeUntilNextMove -= deltaTime;
		if (playerData.timeUntilNextMove <= 0) {
			Vector2i currentCoordinate = headTile.coordinate;
			Vector2i nextCoordinate = new Vector2i(headTile.coordinate.x, headTile.coordinate.y);
			
			switch (playerData.direction) {
				case 0 -> nextCoordinate.y += 1;
				case 1 -> nextCoordinate.x -= 1;
				case 2 -> nextCoordinate.y -= 1;
				case 3 -> nextCoordinate.x += 1;
			}
			
			// Check if player hit bounds
			if (nextCoordinate.x < MIN_GRID_X || nextCoordinate.x >= MAX_GRID_X
				|| nextCoordinate.y < MIN_GRID_Y || nextCoordinate.y >= MAX_GRID_Y) {
				playerData.scene.endGame();
				return;
			}
			
			boolean shouldGrow = playerData.tails.isEmpty();
			
			// Check for collisions
			TileRenderer nextTile = headTile.getGroup().getTile(nextCoordinate);
			if (nextTile != null) {
				Entity entity = nextTile.getEntity();
				if (entity.hasTag(EntityTag.APPLE_TAG)) {
					// Eat apple
					entity.getComponent(AppleData.class).eat();
					shouldGrow = true;
				} else if (entity.hasTag(EntityTag.PLAYER)) {
					// Die
					playerData.scene.endGame();
					return;
				}
			}
			
			// Move player
			headTile.moveTo(nextCoordinate);
			
			// Copy coordinate of last tail tile before it is modified, in case we need it to spawn a new tail tile
			Vector2i newTailCoordinate = currentCoordinate;
			if (shouldGrow && !playerData.tails.isEmpty()) {
				TileRenderer lastTailTile = playerData.tails.getLast().currentTile;
				newTailCoordinate = lastTailTile.coordinate.clone();
			}
			
			// Move each tail segment
			Vector2i lastCoordinate = currentCoordinate;
			for (int i = 0; i < playerData.tails.size(); i++) {
				PlayerTailData tailData = playerData.tails.get(i);
				TileRenderer tailTile = tailData.currentTile;
				Vector2i newCoordinate = lastCoordinate.clone();
				lastCoordinate = tailTile.coordinate;
				
				// Moves the tile from lastCoordinate to newCoordinate
				tailTile.moveTo(newCoordinate);
				tailData.isDirty = true;
			}
			
			// Grow tail
			if (shouldGrow) {
				PlayerTailPrefab playerTailPrefab = new PlayerTailPrefab(newTailCoordinate.clone(), playerData);
				playerTailPrefab.setRegion(0, 0, headTile.size, headTile.size);
				
				// Add tail segment and increase score
				TileRenderer newTailTile = headTile.getGroup().addTile(playerTailPrefab);
				playerData.tails.add(newTailTile.getComponent(PlayerTailData.class));
				playerData.score++;
				
				// Link tail segments
				PlayerTailData tailData = newTailTile.getComponent(PlayerTailData.class);
				if (playerData.tails.size() == 1) {
					tailData.previousTile = headTile;
				} else {
					TileRenderer previousTile = playerData.tails.get(playerData.tails.size() - 2).currentTile;
					tailData.previousTile = previousTile;
					previousTile.getComponent(PlayerTailData.class).nextTile = newTailTile;
				}
			}
			
			// Change sprite based on current direction
			int spriteRegionY = GameScene.CELL_SIZE * (3 - playerData.direction);
			sprite.setRegion(0, spriteRegionY, GameScene.CELL_SIZE, GameScene.CELL_SIZE);
			
			// Reset timer
			playerData.timeUntilNextMove += PlayerData.TIME_BETWEEN_MOVES;
			
			playerData.previousMoveDirection = playerData.direction;
		}
		
		// Interpolate movement between tiles
		float offset = (0.5f - playerData.timeUntilNextMove / PlayerData.TIME_BETWEEN_MOVES) * GameScene.CELL_SIZE;
		sprite.offset.x = 0;
		sprite.offset.y = 0;
		switch (playerData.direction) {
			case 0 -> sprite.offset.y = offset;
			case 1 -> sprite.offset.x = -offset;
			case 2 -> sprite.offset.y = -offset;
			case 3 -> sprite.offset.x = offset;
		}
	}
}
