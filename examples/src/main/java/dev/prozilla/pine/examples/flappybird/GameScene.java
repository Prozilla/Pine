package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.core.object.canvas.Canvas;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.examples.flappybird.object.*;

import java.util.Random;

public class GameScene extends dev.prozilla.pine.core.state.Scene {
	
	public boolean gameOver;
	public int playerScore;
	
	private float timeUntilNextObstacle;
	
	// Game objects
	private GameObject obstacles;
	public Player player;
	public GameOverText gameOverText;
	
	// Common resources
	public Font font;
	
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
		player = (Player)add(new Player(game));
		
		// Prepare obstacles
		obstacles = add(new GameObject(game));
		
		// Create user interface
		font = ResourcePool.loadFont("flappybird/flappy-bird.ttf", 32);
		Canvas canvas = (Canvas)add(new Canvas(game));
		canvas.addChild(new ScoreText(game));
		gameOverText = (GameOverText)canvas.addChild(new GameOverText(game));
		
		timeUntilNextObstacle = 0;
		gameOver = false;
		playerScore = 0;
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		if (gameOver) {
			if (getInput().getKeyDown(Key.R)) {
				game.reloadScene();
			}
		}
	}
	
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		super.update(deltaTime);
		
		if (!gameOver) {
			if (timeUntilNextObstacle <= 0) {
				// Spawn obstacle
				obstacles.addChild(new Pipes(game));
				timeUntilNextObstacle = random.nextFloat(MIN_OBSTACLE_TIME, MAX_OBSTACLE_TIME);
			} else {
				// Decrease time until next obstacle
				timeUntilNextObstacle -= deltaTime;
			}
		}
	}
	
	public void endGame() {
		gameOver = true;
		player.resetVelocity();
		gameOverText.setActive(true);
	}
}
