package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.snake.EntityTag;

public class ApplePrefab extends TilePrefab {
	
	public ApplePrefab() {
		super("snake/apple.png");
		setName("Apple");
		setTag(EntityTag.APPLE_TAG);
	}
}
