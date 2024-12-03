package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

import java.util.Random;

public class Pipes extends Entity {
	
	private final Pipe bottomPipe;
	private final Pipe topPipe;
	private boolean passed;
	
	private GameScene gameScene;
	
	private static final Random random = new Random();
	
	// Constants
	public static final float SPEED = 200f;
	
	public Pipes(World world) {
		super(world);
		
		// Randomize height and gap
		int height = Math.round(random.nextFloat(Main.HEIGHT / -4f, Main.HEIGHT / 4f));
		int gap = random.nextInt(150, 200);
		
		// Add pipes
		bottomPipe = (Pipe)addChild(new Pipe(world, false));
		topPipe = (Pipe)addChild(new Pipe(world, true));
		
		// Position pipes
		topPipe.transform.y = height + gap / 2f;
		bottomPipe.transform.y = height - gap / 2f - Pipe.HEIGHT * 1.5f;
		
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
			float newX = bottomPipe.transform.x - deltaTime * SPEED;
			bottomPipe.transform.x = newX;
			topPipe.transform.x = newX;
			
			// Check if player hit one of the pipes
			if (gameScene.player.transform.x + PlayerData.WIDTH > newX && gameScene.player.transform.x < newX + Pipe.WIDTH
				&& (gameScene.player.transform.y + PlayerData.HEIGHT > topPipe.transform.y || gameScene.player.transform.y < bottomPipe.transform.y + Pipe.HEIGHT)) {
				gameScene.endGame();
			} else {
				// Check if player has passed through pipes
				if (newX < gameScene.player.transform.x && !passed) {
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
