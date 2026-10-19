package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class ScorePrefab extends TextPrefab {
	
	public ScorePrefab(Font font, GameScene gameScene) {
		super("0");
		setName("ScoreText");
		
		// Set position and appearance
		setAnchor(Anchor.TOP_RIGHT);
		setColor(Color.white());
		setMargin(new DualDimension(16));
		setFont(font);
		
		setText(() -> String.valueOf(gameScene.playerScore));
	}
	
}
