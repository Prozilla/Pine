package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;

public class GameOverPrefab extends TextPrefab {
	
	public GameOverPrefab(Font font) {
		super("Game over");
		setName("GameOverText");
		
		setAnchor(GridAlignment.CENTER);
		setColor(Color.decode("#aa00ff"));
		setFont(font);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.setActive(false);
	}
}
