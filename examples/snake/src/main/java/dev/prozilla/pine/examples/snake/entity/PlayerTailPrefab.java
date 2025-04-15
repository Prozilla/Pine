package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.snake.EntityTag;
import dev.prozilla.pine.examples.snake.component.PlayerData;
import dev.prozilla.pine.examples.snake.component.PlayerTailData;

public class PlayerTailPrefab extends TilePrefab {
	
	PlayerData playerData;
	
	public PlayerTailPrefab(Vector2i coordinate, PlayerData playerData) {
		super("snake/snake_body_straight.png", coordinate);
		setName("PlayerTail");
		setTag(EntityTag.PLAYER);
		
		this.playerData = playerData;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TileRenderer tileRenderer = entity.getComponent(TileRenderer.class);
		entity.addComponent(new PlayerTailData(playerData, tileRenderer));
	}
}
