package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.snake.GameScene;
import dev.prozilla.pine.examples.snake.component.PlayerData;
import dev.prozilla.pine.examples.snake.component.PlayerTailData;

public class PlayerTailUpdater extends UpdateSystem {
	
	public PlayerTailUpdater() {
		super(PlayerTailData.class, TileRenderer.class, SpriteRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		PlayerTailData tailData = chunk.getComponent(PlayerTailData.class);
		TileRenderer tile = chunk.getComponent(TileRenderer.class);
		SpriteRenderer sprite = chunk.getComponent(SpriteRenderer.class);
		
		Vector2i direction = tailData.previousTile.getCoordinate().clone().subtract(tile.getCoordinate().clone());
		
		// Interpolate movement between tiles
		float offset = 0;
		if (!tailData.isCurved) {
			offset = (0.5f - tailData.playerData.timeUntilNextMove / PlayerData.TIME_BETWEEN_MOVES) * GameScene.CELL_SIZE;
		}
		sprite.offset.x = direction.x * offset;
		sprite.offset.y = direction.y * offset;
		
		// Check if the sprite needs to be updated
		if (!tailData.isDirty) {
			return;
		}
		
		// Update tail segment sprite acc
		if (tailData.nextTile == null) {
			// Snake butt segment
			sprite.texture = ResourcePool.loadTexture("snake/snake_tail.png");
			tailData.isCurved = false;
			
			if (direction.y == 1) {
				sprite.regionOffset.y = tile.size * 3;
			} else if (direction.y == -1) {
				sprite.regionOffset.y = tile.size;
			} else if (direction.x == 1) {
				sprite.regionOffset.y = 0;
			} else {
				sprite.regionOffset.y = tile.size * 2;
			}
		} else {
			Vector2i otherDirection = tailData.nextTile.getCoordinate().clone().subtract(tile.getCoordinate().clone());
			float dotProduct = direction.dot(otherDirection);
			
			if (dotProduct != 0) {
				// Straight tail segment
				sprite.texture = ResourcePool.loadTexture("snake/snake_body_straight.png");
				tailData.isCurved = false;
				
				if (direction.x != 0) {
					sprite.regionOffset.y = tile.size;
				} else {
					sprite.regionOffset.y = 0;
				}
			} else {
				// Curved tail segment
				sprite.texture = ResourcePool.loadTexture("snake/snake_body_curved.png");
				tailData.isCurved = true;
				
				direction.add(otherDirection);
				
				if (direction.x == -1) {
					if (direction.y == -1) {
						sprite.regionOffset.y = 0;
					} else {
						sprite.regionOffset.y = tile.size;
					}
				} else {
					if (direction.y == -1) {
						sprite.regionOffset.y = tile.size * 3;
					} else {
						sprite.regionOffset.y = tile.size * 2;
					}
				}
			}
		}
		
		tailData.isDirty = false;
	}
}
