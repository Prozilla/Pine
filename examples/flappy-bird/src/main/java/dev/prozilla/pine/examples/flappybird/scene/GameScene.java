package dev.prozilla.pine.examples.flappybird.scene;

import dev.prozilla.pine.common.system.Directory;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
import dev.prozilla.pine.examples.flappybird.entity.GameOverPrefab;
import dev.prozilla.pine.examples.flappybird.entity.PipesPrefab;
import dev.prozilla.pine.examples.flappybird.entity.PlayerPrefab;
import dev.prozilla.pine.examples.flappybird.entity.ScorePrefab;
import dev.prozilla.pine.examples.flappybird.system.background.BackgroundMover;
import dev.prozilla.pine.examples.flappybird.system.canvas.ScoreTextUpdater;
import dev.prozilla.pine.examples.flappybird.system.obstacle.PipeInitializer;
import dev.prozilla.pine.examples.flappybird.system.obstacle.PipesInitializer;
import dev.prozilla.pine.examples.flappybird.system.obstacle.PipesMover;
import dev.prozilla.pine.examples.flappybird.system.player.PlayerInitializer;
import dev.prozilla.pine.examples.flappybird.system.player.PlayerInputHandler;
import dev.prozilla.pine.examples.flappybird.system.player.PlayerMover;

public class GameScene extends SceneBase {
	
	// Game state
	public boolean gameOver;
	public int playerScore;
	
	private Timer.Interval obstacleSpawnInterval;
	
	// Prefabs
	private PipesPrefab pipesPrefab;
	
	// Entities
	private Entity obstacles;
	public Entity player;
	public Entity gameOverText;
	
	// Data
	public Directory dataDirectory;
	
	// Constants
	public static final float MIN_OBSTACLE_TIME = 0.75f;
	public static final float MAX_OBSTACLE_TIME = 2.5f;
	
	@Override
	protected void load() {
		super.load();
		
		dataDirectory = new Directory("data");
		
		// Create prefabs for entities
		PlayerPrefab playerPrefab = new PlayerPrefab();
		ScorePrefab scorePrefab = new ScorePrefab(font);
		GameOverPrefab gameOverPrefab = new GameOverPrefab(font);
		pipesPrefab = new PipesPrefab();
		
		// Add systems
		world.addSystem(new PlayerInitializer());
		world.addSystem(new PlayerInputHandler());
		world.addSystem(new PlayerMover());
		world.addSystem(new BackgroundMover());
		world.addSystem(new PipeInitializer());
		world.addSystem(new PipesInitializer());
		world.addSystem(new PipesMover());
		world.addSystem(new ScoreTextUpdater());
		
		// Create player object
		player = world.addEntity(playerPrefab);
		
		// Create empty parent for obstacles
		obstacles = world.addEntity(new Entity(world));
		
		// Create user interface
		Entity nodeRoot = world.addEntity(new NodeRootPrefab());
		nodeRoot.addChild(scorePrefab);
		gameOverText = nodeRoot.addChild(gameOverPrefab);
		
		// Set default values
		gameOver = false;
		playerScore = 0;
		
		// Start spawning obstacles
		obstacleSpawnInterval = getTimer().startRandomInterval(this::spawnObstacle, MIN_OBSTACLE_TIME, MAX_OBSTACLE_TIME, true);
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		if (gameOver && (getInput().getKeyDown(Key.R) || getInput().getGamepad().getButtonDown(GamepadButton.Y))) {
			application.reloadScene();
		}
	}
	
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		if (gameOver && !gameOverText.isActive()) {
			gameOverText.setActive(true);
			gameOverText.getChildWithTag(EntityTag.FINAL_SCORE_TAG).getComponent(TextNode.class).setText(String.format("Your Score: %s", playerScore));
		}
		
		super.update(deltaTime);
		
		if (gameOver) {
			obstacleSpawnInterval.destroy();
		}
	}
	
	public void spawnObstacle() {
		obstacles.addChild(pipesPrefab.instantiate(world));
	}
	
	public void endGame() {
		gameOver = true;
		player.getComponent(PlayerData.class).resetVelocity();
	}
}
