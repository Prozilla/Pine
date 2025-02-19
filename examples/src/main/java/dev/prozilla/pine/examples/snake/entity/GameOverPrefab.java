package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;

public class GameOverPrefab extends TextPrefab {
	
	public GameOverPrefab(Font font) {
		super("Game over");
		setName("GameOverText");
		
		setAnchor(RectTransform.Anchor.CENTER);
		setColor(Color.decode("#aa00ff"));
		setFont(font);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.setActive(false);
	}
}
