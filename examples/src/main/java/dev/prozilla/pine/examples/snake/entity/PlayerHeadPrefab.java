package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.snake.component.PlayerData;

public class PlayerHeadPrefab extends TilePrefab {
	
	public PlayerHeadPrefab() {
		super("snake/snake_head.png");
		setName("PlayerHead");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new PlayerData());
	}
}
