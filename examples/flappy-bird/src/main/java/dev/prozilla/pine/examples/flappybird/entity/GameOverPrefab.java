package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionParser;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.flappybird.EntityTag;

public class GameOverPrefab extends LayoutPrefab {
	
	protected Font font;
	
	public GameOverPrefab(Font font) {
		setName("GameOver");
		setAnchor(GridAlignment.TOP);
		setDirection(Direction.DOWN);
		setAlignment(EdgeAlignment.CENTER);
		setMargin(new Dimension(), new DimensionParser().read("33vh"));
		setGap(new Dimension(16));
	
		this.font = font;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.tag = EntityTag.GAME_OVER_TAG;
		
		TextPrefab gameOverText = new TextPrefab("Game Over");
		gameOverText.setColor(Color.white());
		gameOverText.setFont(font.setSize(48));
		entity.addChild(gameOverText);
		
		TextPrefab restartText = new TextPrefab("Press 'R' to Restart.");
		restartText.setColor(Color.white());
		restartText.setFont(font.setSize(16));
		entity.addChild(restartText);
		
		TextPrefab scoreText = new TextPrefab("Your Score: 0");
		scoreText.setColor(Color.white());
		scoreText.setFont(font.setSize(16));
		scoreText.setTag(EntityTag.FINAL_SCORE_TAG);
		entity.addChild(scoreText);
		
		entity.setActive(false);
	}
}
