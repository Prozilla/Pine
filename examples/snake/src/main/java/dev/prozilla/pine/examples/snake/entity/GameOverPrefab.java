package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;

public class GameOverPrefab extends TextPrefab {
	
	public GameOverPrefab(Font font) {
		super("Game over");
		setName("GameOverText");
		
		removeDefaultStyleSheet();
		setAnchor(Anchor.CENTER);
		setColor(Color.rebeccaPurple());
		setFont(font);
		setFontSize(new Dimension(64));
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.setActive(false);
	}
}
