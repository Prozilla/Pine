package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.Scene;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.CanvasPrefab;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
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

public class GameScene extends Scene {
	
	// Game state
	public boolean gameOver;
	public int playerScore;
	
	private float timeUntilNextObstacle;
	
	// Prefabs
	private PipesPrefab pipesPrefab;
	
	// Entities
	private Entity obstacles;
	public Entity player;
	public Entity gameOverText;
	
	// Common resources
	public Font font;
	
	private static final Random random = new Random();
	
	// Constants
	public static final float MIN_OBSTACLE_TIME = 0.75f;
	public static final float MAX_OBSTACLE_TIME = 3.5f;
	
	@Override
	protected void load() {
		super.load();
		
		// Create prefabs for entities
		BackgroundPrefab backgroundPrefab = new BackgroundPrefab();
		PlayerPrefab playerPrefab = new PlayerPrefab();
		ScorePrefab scorePrefab = new ScorePrefab();
		GameOverPrefab gameOverPrefab = new GameOverPrefab();
		pipesPrefab = new PipesPrefab();
		
		// Load resources
		font = ResourcePool.loadFont("flappybird/flappy-bird.ttf", 32);
		
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
		int backgroundCount = Math.round((float)Main.WIDTH / BackgroundData.WIDTH + 0.5f) + 1;
		for (int i = 0; i < backgroundCount; i++) {
			world.addEntity(backgroundPrefab.instantiate(world, i));
		}
		
		// Create player object
		player = world.addEntity(playerPrefab);
		
		// Create empty parent for obstacles
		obstacles = world.addEntity(new Entity(world));
		
		// Create user interface
		Entity canvas = world.addEntity(new CanvasPrefab());
		canvas.addChild(scorePrefab);
		gameOverText = canvas.addChild(gameOverPrefab);
		
		// Set default values
		timeUntilNextObstacle = 0;
		gameOver = false;
		playerScore = 0;
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		if (gameOver) {
			if (application.getInput().getKeyDown(Key.R)) {
				application.reloadScene();
			}
		}
	}
	
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		if (gameOver) {
			gameOverText.setActive(true);
		}

		super.update(deltaTime);
		
		if (!gameOver) {
			if (timeUntilNextObstacle <= 0) {
				// Spawn obstacle
				obstacles.addChild(pipesPrefab.instantiate(world));
				timeUntilNextObstacle += random.nextFloat(MIN_OBSTACLE_TIME, MAX_OBSTACLE_TIME);
			} else {
				// Decrease time until next obstacle
				timeUntilNextObstacle -= deltaTime;
			}
		}
	}
	
	public void endGame() {
		gameOver = true;
		player.getComponent(PlayerData.class).resetVelocity();
	}
}
