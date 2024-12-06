package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.canvas.Text;
import dev.prozilla.pine.examples.flappybird.EntityTag;

public class GameOverText extends Text {
	
	public GameOverText(World world) {
		super(world, "Game Over");

		tag = EntityTag.GAME_OVER_TAG;
		
		// Set position and appearance
		setAnchor(RectTransform.Anchor.CENTER);
		setColor(Color.WHITE);
	}
}
