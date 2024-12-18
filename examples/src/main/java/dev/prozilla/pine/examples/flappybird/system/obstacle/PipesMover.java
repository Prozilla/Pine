package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

/**
 * Moves pipe pairs along the horizontal axis and checks whether the player has passed or hit pipes.
 */
public class PipesMover extends UpdateSystem {
	
	public PipesMover() {
		super(PipesData.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		PipesData pipesData = chunk.getComponent(PipesData.class);
		
		GameScene gameScene = pipesData.gameScene;
		
		if (!gameScene.gameOver) {
			// Scroll position of pipes to the left
			float scrollX = deltaTime * PipesData.SPEED;
			float newX = pipesData.bottomPipePrefab.transform.x - scrollX;
			pipesData.bottomPipePrefab.transform.x = newX;
			pipesData.topPipePrefab.transform.x = newX;
			
			// Check if player hit one of the pipes
			if (gameScene.player.transform.x + PlayerData.WIDTH > newX && gameScene.player.transform.x < newX + PipeData.WIDTH
			     && (gameScene.player.transform.y + PlayerData.HEIGHT > pipesData.topPipePrefab.transform.y || gameScene.player.transform.y < pipesData.bottomPipePrefab.transform.y + PipeData.HEIGHT)) {
				gameScene.endGame();
			} else {
				// Check if player has passed through pipes
				if (newX < gameScene.player.transform.x && !pipesData.passed) {
					gameScene.playerScore++;
					pipesData.passed = true;
				}
			}
		}
	}
}
