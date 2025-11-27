package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionParser;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
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
		setTag(EntityTag.GAME_OVER_TAG);
		setBackgroundColor(Color.white());
		setPadding(new DualDimension(32, 16));
		setActive(false);
		
		TextPrefab gameOverText = new TextPrefab("Game Over");
		gameOverText.setColor(Color.black());
		gameOverText.setFont(font.setSize(48));
		
		TextPrefab restartText = new TextPrefab("Press 'R' to Restart.");
		restartText.setColor(Color.black());
		restartText.setFont(font.setSize(16));
		
		TextPrefab scoreText = new TextPrefab("Your Score: 0");
		scoreText.setColor(Color.black());
		scoreText.setFont(font.setSize(16));
		scoreText.setTag(EntityTag.FINAL_SCORE_TAG);
		
		TextButtonPrefab restartButton = new TextButtonPrefab("Restart");
		restartButton.setColor(Color.white());
		restartButton.setBackgroundColor(Color.black());
		restartButton.setFont(font.setSize(16));
		restartButton.setPadding(new DualDimension(16, 4));
		restartButton.setClickCallback(ApplicationProvider::reloadScene);
		
		TextButtonPrefab menuButton = new TextButtonPrefab("Back to Menu");
		menuButton.setColor(Color.white());
		menuButton.setBackgroundColor(Color.black());
		menuButton.setFont(font.setSize(16));
		menuButton.setPadding(new DualDimension(16, 4));
		menuButton.setClickCallback(ApplicationProvider::nextScene);
		
		LayoutPrefab buttonRow = new LayoutPrefab();
		buttonRow.setDirection(Direction.RIGHT);
		buttonRow.setGap(new Dimension(8));
		
		buttonRow.addChildren(restartButton, menuButton);

		addChildren(gameOverText, restartText, scoreText, buttonRow);
		
		this.font = font;
	}
	
}
