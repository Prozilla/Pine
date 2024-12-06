package dev.prozilla.pine.examples.flappybird.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.system.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.component.ScoreTextData;

public class ScoreTextUpdater extends UpdateSystem {
	
	public ScoreTextUpdater() {
		super(ScoreTextData.class, TextRenderer.class);
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
			ScoreTextData scoreTextData = match.getComponent(ScoreTextData.class);
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			// Update text field based on score value
			textRenderer.setText(String.valueOf(scoreTextData.gameScene.playerScore));
		});
	}
}
