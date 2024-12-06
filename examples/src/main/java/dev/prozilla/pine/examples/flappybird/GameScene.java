package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.canvas.Canvas;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;
import dev.prozilla.pine.examples.flappybird.entity.*;
import dev.prozilla.pine.examples.flappybird.system.background.BackgroundInitializer;
import dev.prozilla.pine.examples.flappybird.system.background.BackgroundMover;
import dev.prozilla.pine.examples.flappybird.system.canvas.GameOverTextInitializer;
import dev.prozilla.pine.examples.flappybird.system.canvas.ScoreTextInitializer;
import dev.prozilla.pine.examples.flappybird.system.canvas.ScoreTextUpdater;
import dev.prozilla.pine.examples.flappybird.system.obstacle.PipeInitializer;
import dev.prozilla.pine.examples.flappybird.system.obstacle.PipesInitializer;
import dev.prozilla.pine.examples.flappybird.system.obstacle.PipesMover;
import dev.prozilla.pine.examples.flappybird.system.player.PlayerInitializer;
import dev.prozilla.pine.examples.flappybird.system.player.PlayerInputHandler;
import dev.prozilla.pine.examples.flappybird.system.player.PlayerMover;

import java.util.Random;

public class GameScene extends dev.prozilla.pine.core.state.Scene {
	
	public boolean gameOver;
	public int playerScore;
	
	private float timeUntilNextObstacle;
	
	// Game objects
	private Entity obstacles;
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
		
		// Add systems
		world.addSystem(new PlayerInitializer());
		world.addSystem(new PlayerInputHandler());
		world.addSystem(new PlayerMover());
		world.addSystem(new BackgroundInitializer());
		world.addSystem(new BackgroundMover());
		world.addSystem(new PipeInitializer());
		world.addSystem(new PipesInitializer());
		world.addSystem(new PipesMover());
		world.addSystem(new ScoreTextInitializer());
		world.addSystem(new ScoreTextUpdater());
		world.addSystem(new GameOverTextInitializer());
		
		// Fill screen with background sprites
		Background[] backgrounds = new Background[Math.round((float)Main.WIDTH / BackgroundData.WIDTH + 0.5f) + 1];
		for (int i = 0; i < backgrounds.length; i++) {
			Background background = new Background(world, i);
			add(background);
			backgrounds[i] = background;
		}
		
		// Create player object
		player = (Player)add(new Player(world));
		
		// Prepare obstacles
		obstacles = add(new Entity(world));
		
		// Create user interface
		font = ResourcePool.loadFont("flappybird/flappy-bird.ttf", 32);
		Canvas canvas = (Canvas)add(new Canvas(world));
		canvas.addChild(new ScoreText(world));
		gameOverText = (GameOverText)canvas.addChild(new GameOverText(world));
		
		timeUntilNextObstacle = 0;
		gameOver = false;
		playerScore = 0;
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		if (gameOver) {
			if (getInput().getKeyDown(Key.R)) {
				application.reloadScene();
			}
		}
	}
	
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		super.update(deltaTime);
		
		if (!gameOver) {
			if (timeUntilNextObstacle <= 0) {
				// Spawn obstacle
				obstacles.addChild(new Pipes(world));
				timeUntilNextObstacle = random.nextFloat(MIN_OBSTACLE_TIME, MAX_OBSTACLE_TIME);
			} else {
				// Decrease time until next obstacle
				timeUntilNextObstacle -= deltaTime;
			}
		}
	}
	
	public void endGame() {
		gameOver = true;
		player.playerData.resetVelocity();
		gameOverText.setActive(true);
	}
}
