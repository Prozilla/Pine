package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.snake.GameScene;

public class BackgroundPrefab extends TilePrefab {
	
	public BackgroundPrefab() {
		super("snake/background.png");
		setName("Background");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		int variant = (Math.abs(coordinate.x) % 2 + Math.abs(coordinate.y) % 2) % 2;
		
		SpriteRenderer sprite = entity.getComponent(SpriteRenderer.class);
		sprite.setRegion(0, variant * GameScene.CELL_SIZE, GameScene.CELL_SIZE, GameScene.CELL_SIZE);
	}
}
