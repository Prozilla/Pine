package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;

import java.util.Random;

public class Pipes extends GameObject {
	
	private final Pipe bottomPipe;
	private final Pipe topPipe;
	private boolean passed;
	
	private GameScene gameScene;
	
	private static final Random random = new Random();
	
	// Constants
	public static final float SPEED = 200f;
	
	public Pipes(Game game) {
		super(game);
		
		// Randomize height and gap
		int height = Math.round(random.nextFloat(Main.HEIGHT / -4f, Main.HEIGHT / 4f));
		int gap = random.nextInt(150, 200);
		
		// Add pipes
		bottomPipe = (Pipe)addChild(new Pipe(game, false));
		topPipe = (Pipe)addChild(new Pipe(game, true));
		
		// Position pipes
		topPipe.y = height + gap / 2f;
		bottomPipe.y = height - gap / 2f - Pipe.HEIGHT * 1.5f;
		
		passed = false;
	}
	
	@Override
	public void init(long window) throws IllegalStateException {
		super.init(window);
		
		// Store reference to scene
		gameScene = (GameScene)scene;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (!gameScene.gameOver) {
			// Scroll position of pipes to the left
			float newX = bottomPipe.x - deltaTime * SPEED;
			bottomPipe.x = newX;
			topPipe.x = newX;
			
			// Check if player hit one of the pipes
			if (gameScene.player.x + Player.WIDTH > newX && gameScene.player.x < newX + Pipe.WIDTH
				&& (gameScene.player.y + Player.HEIGHT > topPipe.y || gameScene.player.y < bottomPipe.y + Pipe.HEIGHT)) {
				gameScene.endGame();
			} else {
				// Check if player has passed through pipes
				if (newX < gameScene.player.x && !passed) {
					gameScene.playerScore++;
					passed = true;
				}
			}
		}
	}
	
	@Override
	public String getName() {
		return getName("Pipes");
	}
}
