package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;
import dev.prozilla.pine.examples.snake.GameScene;

public class BackgroundPrefab extends SpritePrefab {
	
	public BackgroundPrefab() {
		super("snake/background.png");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		int variant = (Math.abs((int)Math.floor(entity.transform.x / GameScene.CELL_SIZE)) % 2 + Math.abs((int)Math.floor(entity.transform.y / GameScene.CELL_SIZE)) % 2) % 2;
		
		SpriteRenderer sprite = entity.getComponent(SpriteRenderer.class);
		sprite.setRegion(0, variant * GameScene.CELL_SIZE, GameScene.CELL_SIZE, GameScene.CELL_SIZE);
	}
}
