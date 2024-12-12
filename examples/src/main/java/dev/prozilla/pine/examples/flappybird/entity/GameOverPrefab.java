package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.canvas.Text;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;
import dev.prozilla.pine.examples.flappybird.EntityTag;

public class GameOverPrefab extends TextPrefab {
	
	public GameOverPrefab() {
		super("Game Over");

		// Set position and appearance
		setAnchor(RectTransform.Anchor.CENTER);
		setColor(Color.WHITE);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.tag = EntityTag.GAME_OVER_TAG;
	}
}