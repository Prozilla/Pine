package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;
import dev.prozilla.pine.examples.sokoban.util.Command;
import dev.prozilla.pine.examples.sokoban.util.TileMoveAction;

public class PlayerMover extends UpdateSystem {
	
	public PlayerMover() {
		super(PlayerData.class, SpriteRenderer.class, TileRenderer.class);
		setApplyTimeScale(true);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		TileRenderer tileRenderer = chunk.getComponent(TileRenderer.class);
		
		// Animate sprite
		if (playerData.direction != null) {
			String[] sprites = PlayerData.directionToSprites.get(playerData.direction);
			
			String sprite;
			if (playerData.canMove) {
				int spriteIndex = Math.round((playerData.timeUntilMoveCompletes / PlayerData.TIME_TO_MOVE) * (sprites.length - 1));
				sprite = sprites[spriteIndex];
			} else {
				sprite = sprites[0];
			}
			
			spriteRenderer.texture = ResourcePool.loadTexture(sprite);
		}
		
		// Move player
		if (playerData.canMove && playerData.direction != null) {
			if (playerData.timeUntilMoveCompletes > deltaTime) {
				// Animate movement
				float movementFactor = 1 - (playerData.timeUntilMoveCompletes / PlayerData.TIME_TO_MOVE);
				movementFactor *= tileRenderer.size;
				spriteRenderer.offset.x = playerData.direction.x * movementFactor;
				spriteRenderer.offset.y = playerData.direction.y * movementFactor;
			} else {
				// Finish movement
				spriteRenderer.offset.x = 0;
				spriteRenderer.offset.y = 0;
				
				TileMoveAction playerMoveAction = new TileMoveAction(tileRenderer, playerData.direction.x, playerData.direction.y);
				TileMoveAction crateMoveAction = null;
				
				if (playerData.pushingCrateTile != null) {
					crateMoveAction = new TileMoveAction(playerData.pushingCrateTile, playerData.direction.x, playerData.direction.y);
					playerData.pushingCrateTile.moveBy(playerData.direction.x, playerData.direction.y);
				}
				
				tileRenderer.moveBy(playerData.direction.x, playerData.direction.y);
				playerData.direction = null;
				playerData.history.push(new Command(playerMoveAction, crateMoveAction));
			}
			
			// Animate movement of crate
			if (playerData.pushingCrateSprite != null) {
				playerData.pushingCrateSprite.offset.x = spriteRenderer.offset.x;
				playerData.pushingCrateSprite.offset.y = spriteRenderer.offset.y;
			}
			
			playerData.timeUntilMoveCompletes -= deltaTime;
		} else {
			playerData.timeUntilMoveCompletes = 0;
		}
	}
}
