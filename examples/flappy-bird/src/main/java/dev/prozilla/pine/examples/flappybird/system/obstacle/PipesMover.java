package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

/**
 * Moves pipe pairs along the horizontal axis and checks whether the player has passed or hit pipes.
 */
public class PipesMover extends UpdateSystem {
	
	public PipesMover() {
		super(PipesData.class, AudioEffectPlayer.class);
		setApplyTimeScale(true);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		PipesData pipesData = chunk.getComponent(PipesData.class);
		AudioEffectPlayer audioEffectPlayer = chunk.getComponent(AudioEffectPlayer.class);
		
		GameScene gameScene = pipesData.gameScene;
		
		if (!gameScene.gameOver) {
			// Scroll position of pipes to the left
			float scrollX = deltaTime * PipesData.SPEED;
			float newX = pipesData.bottomPipe.transform.position.x - scrollX;
			pipesData.bottomPipe.transform.position.x = newX;
			pipesData.topPipe.transform.position.x = newX;
			
			// Check if player hit one of the pipes
			if (gameScene.playerData.collider.collidesWith(pipesData.topPipeData.collider)
				|| gameScene.playerData.collider.collidesWith(pipesData.bottomPipeData.collider)) {
				audioEffectPlayer.playRandom();
				gameScene.endGame();
			} else {
				// Check if player has passed through pipes
				if (newX < gameScene.player.transform.position.x && !pipesData.passed) {
					gameScene.playerScore++;
					pipesData.passed = true;
				}
			}
		}
	}
}
