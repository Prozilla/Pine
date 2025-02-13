package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.snake.EntityTag;
import dev.prozilla.pine.examples.snake.GameScene;
import dev.prozilla.pine.examples.snake.component.PlayerData;

public class PlayerHeadPrefab extends TilePrefab {
	
	protected final GameScene scene;
	
	public PlayerHeadPrefab(GameScene scene) {
		super("snake/snake_head.png");
		setName("PlayerHead");
		setTag(EntityTag.PLAYER);
		
		this.scene = scene;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new PlayerData(scene));
	}
}
