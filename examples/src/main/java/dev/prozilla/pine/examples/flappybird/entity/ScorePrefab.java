package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;
import dev.prozilla.pine.examples.flappybird.EntityTag;

public class ScorePrefab extends TextPrefab {
	
	public ScorePrefab(Font font) {
		super("0");
		setName("ScoreText");
		
		// Set position and appearance
		setAnchor(RectTransform.Anchor.TOP_RIGHT);
		setColor(Color.WHITE);
		setOffset(16, 16);
		setFont(font);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.tag = EntityTag.SCORE_TAG;
	}
}
