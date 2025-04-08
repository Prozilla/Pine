package dev.prozilla.pine.examples.snake;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.component.particle.ParticleBurstEmitter;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.CanvasPrefab;
import dev.prozilla.pine.core.entity.prefab.sprite.GridPrefab;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.examples.snake.entity.*;
import dev.prozilla.pine.examples.snake.system.PlayerInput;
import dev.prozilla.pine.examples.snake.system.PlayerMover;
import dev.prozilla.pine.examples.snake.system.PlayerTailUpdater;

import java.util.Random;

public class GameScene extends Scene {
	
	// Game state
	public boolean gameOver;
	
	// Entities & components
	private GridGroup foregroundGrid;
	private Entity gameOverText;
	
	// Prefabs
	private ApplePrefab applePrefab;
	
	// Resources
	public Font font;
	
	// Timing
	private float timeUntilNextSpawn;
	
	private static final Random random = new Random();
	
	// Constants
	private static final float MIN_TIME_BETWEEN_SPAWNS = 2;
	private static final float MAX_TIME_BETWEEN_SPAWNS = 5;
	
	public static final int CELL_SIZE = 40;
	public static final int SCALE = 1;
	
	public static final int GRID_WIDTH = 20;
	public static final int GRID_HEIGHT = 20;
	
	public static final int WORLD_WIDTH = CELL_SIZE * GRID_WIDTH * SCALE;
	public static final int WORLD_HEIGHT = CELL_SIZE * GRID_HEIGHT * SCALE;
	public static final int MIN_GRID_X = GRID_WIDTH / -2;
	public static final int MIN_GRID_Y = GRID_HEIGHT / -2;
	public static final int MAX_GRID_X = GRID_WIDTH / 2;
	public static final int MAX_GRID_Y = GRID_HEIGHT / 2;
	
	@Override
	protected void load() {
		super.load();
		
		font = ResourcePool.loadFont("snake/monomaniac.ttf", 64);
		
		// Create prefabs
		PlayerHeadPrefab playerHeadPrefab = new PlayerHeadPrefab(this);
		BackgroundPrefab backgroundPrefab = new BackgroundPrefab();
		applePrefab = new ApplePrefab();
		
		// Add systems to world
		world.addSystem(new PlayerInput());
		world.addSystem(new PlayerMover());
		world.addSystem(new PlayerTailUpdater());
		
		// Create grid prefabs
		GridPrefab backgroundGridPrefab = new GridPrefab(CELL_SIZE);
		backgroundGridPrefab.setName("BackgroundGrid");
		GridPrefab foregroundGridPrefab = new GridPrefab(CELL_SIZE);
		foregroundGridPrefab.setName("ForegroundGrid");
		
		// Add background grid filled with tiles
		GridGroup backgroundGrid = world.addEntity(backgroundGridPrefab).getComponent(GridGroup.class);
		for (int x = MIN_GRID_X; x < MAX_GRID_X; x++) {
			for (int y = MIN_GRID_Y; y < MAX_GRID_Y; y++) {
				backgroundGrid.addTile(backgroundPrefab, x, y);
			}
		}
		
		// Add foreground grid with player
		foregroundGrid = world.addEntity(foregroundGridPrefab).getComponent(GridGroup.class);
		foregroundGrid.addTile(playerHeadPrefab, new Vector2i(0, MIN_GRID_Y));

		// Add particle emitter
		ParticleBurstEmitter appleParticleEmitter = world.addEntity(new AppleParticleEmitterPrefab()).getComponent(ParticleBurstEmitter.class);
		applePrefab.setParticleEmitter(appleParticleEmitter);
		
		// Add user interface
		Entity canvas = world.addEntity(new CanvasPrefab());
		gameOverText = canvas.addChild(new GameOverPrefab(font));
		
		// Reset state
		timeUntilNextSpawn = MIN_TIME_BETWEEN_SPAWNS;
		gameOver = false;
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
		gameOverText.setActive(gameOver);
		
		super.update(deltaTime);
		
		if (!gameOver) {
			timeUntilNextSpawn -= deltaTime;
			if (timeUntilNextSpawn <= 0) {
				Vector2i appleCoordinate = new Vector2i();

				// Search for valid spawn coordinate
				do {
					appleCoordinate.x = random.nextInt(MIN_GRID_X, MAX_GRID_X);
					appleCoordinate.y = random.nextInt(MIN_GRID_Y, MAX_GRID_Y);
				} while (foregroundGrid.coordinateToTile.containsKey(appleCoordinate));
				
				// Spawn apple
				foregroundGrid.addTile(applePrefab.instantiate(world, appleCoordinate));
				
				// Reset apple timer
				timeUntilNextSpawn += random.nextFloat(MIN_TIME_BETWEEN_SPAWNS, MAX_TIME_BETWEEN_SPAWNS);
			}
		}
	}
	
	public void endGame() {
		gameOver = true;
		logger.log("Player died");
	}
	
}
