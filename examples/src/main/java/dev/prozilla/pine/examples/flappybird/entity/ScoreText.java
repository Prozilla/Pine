package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.canvas.Text;
import dev.prozilla.pine.examples.flappybird.EntityTag;

public class ScoreText extends Text {
	
	public ScoreText(World world) {
		super(world, "0");
		
		tag = EntityTag.SCORE_TAG;
		
		// Set position and appearance
		setAnchor(RectTransform.Anchor.TOP_RIGHT);
		setColor(Color.WHITE);
		setOffset(16, 16);
	}
	
	@Override
	public String getName() {
		return getName("ScoreText");
	}
}
