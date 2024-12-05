package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.system.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class PipesMover extends UpdateSystem {
	
	public PipesMover() {
		super(new EntityQuery(PipesData.class));
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
			PipesData pipesData = match.getComponent(PipesData.class);
			
			GameScene gameScene = pipesData.gameScene;
			
			if (!gameScene.gameOver) {
				// Scroll position of pipes to the left
				float scrollX = deltaTime * PipesData.SPEED;
				float newX = pipesData.bottomPipe.transform.x - scrollX;
				pipesData.bottomPipe.transform.x = newX;
				pipesData.topPipe.transform.x = newX;
				
				// Check if player hit one of the pipes
				if (gameScene.player.transform.x + PlayerData.WIDTH > newX && gameScene.player.transform.x < newX + PipeData.WIDTH
				     && (gameScene.player.transform.y + PlayerData.HEIGHT > pipesData.topPipe.transform.y || gameScene.player.transform.y < pipesData.bottomPipe.transform.y + PipeData.HEIGHT)) {
					gameScene.endGame();
				} else {
					// Check if player has passed through pipes
					if (newX < gameScene.player.transform.x && !pipesData.passed) {
						gameScene.playerScore++;
						pipesData.passed = true;
					}
				}
			}
		});
	}
}
