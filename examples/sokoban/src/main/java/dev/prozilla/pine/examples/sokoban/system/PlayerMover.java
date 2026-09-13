package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.component.mesh.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;

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
			
			spriteRenderer.texture = AssetPools.textures.load(sprite);
		}
		
		// Move player
		if (playerData.canMove && playerData.direction != null) {
			if (playerData.timeUntilMoveCompletes > deltaTime) {
				// Animate movement
				float movementFactor = 1 - (playerData.timeUntilMoveCompletes / PlayerData.TIME_TO_MOVE);
				movementFactor *= tileRenderer.size;
				spriteRenderer.getMesh().setOffset(playerData.direction.x * movementFactor, playerData.direction.y * movementFactor);
			} else {
				// Finish movement
				playerData.finishMove();
			}
			
			// Animate movement of crate
			if (playerData.pushingCrateSprite != null) {
				playerData.pushingCrateSprite.getMesh().setOffset(spriteRenderer.getMesh().getOffset());
			}
			
			playerData.timeUntilMoveCompletes -= deltaTime;
		} else {
			playerData.timeUntilMoveCompletes = 0;
		}
	}
	
}
