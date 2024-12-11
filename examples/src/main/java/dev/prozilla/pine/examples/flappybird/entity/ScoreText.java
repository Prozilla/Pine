package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.canvas.Text;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;
import dev.prozilla.pine.examples.flappybird.EntityTag;

public class ScoreText extends TextPrefab {
	
	public ScoreText() {
		super("0");
		
		// Set position and appearance
		setAnchor(RectTransform.Anchor.TOP_RIGHT);
		setColor(Color.WHITE);
		setOffset(16, 16);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.tag = EntityTag.SCORE_TAG;
	}
}
