package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.examples.flappybird.object.Background;
import dev.prozilla.pine.examples.flappybird.object.Pipes;
import dev.prozilla.pine.examples.flappybird.object.Player;

import java.util.Random;

public class Scene extends dev.prozilla.pine.core.state.Scene {
	
	public boolean gameOver;
	
	private float timeUntilNextObstacle;
	
	private static final Random random = new Random();
	
	// Constants
	public static final float MIN_OBSTACLE_TIME = 0.75f;
	public static final float MAX_OBSTACLE_TIME = 3.5f;
	
	@Override
	protected void load() {
		super.load();
		
		// Fill screen with background sprites
		Background[] backgrounds = new Background[Math.round((float)Main.WIDTH / Background.WIDTH + 0.5f) + 1];
		for (int i = 0; i < backgrounds.length; i++) {
			Background background = new Background(game, i);
			add(background);
			backgrounds[i] = background;
		}
		
		// Create player object
		add(new Player(game));
		
		timeUntilNextObstacle = 0;
		gameOver = false;
	}
	
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		super.update(deltaTime);
		
		if (!gameOver) {
			if (timeUntilNextObstacle <= 0) {
				// Spawn obstacle
				add(new Pipes(game));
				timeUntilNextObstacle = random.nextFloat(MIN_OBSTACLE_TIME, MAX_OBSTACLE_TIME);
			} else {
				// Decrease time until next obstacle
				timeUntilNextObstacle -= deltaTime;
			}
		}
	}
	
	public void endGame() {
		gameOver = true;
	}
}
