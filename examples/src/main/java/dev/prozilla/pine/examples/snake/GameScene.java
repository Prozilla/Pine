package dev.prozilla.pine.examples.snake;

import dev.prozilla.pine.core.Scene;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.prefab.sprite.GridPrefab;
import dev.prozilla.pine.examples.snake.entity.ApplePrefab;
import dev.prozilla.pine.examples.snake.entity.BackgroundPrefab;
import dev.prozilla.pine.examples.snake.entity.PlayerHeadPrefab;
import dev.prozilla.pine.examples.snake.system.PlayerHeadMover;
import dev.prozilla.pine.examples.snake.system.PlayerInput;

import java.util.Random;

public class GameScene extends Scene {
	
	// Grids
	private GridGroup foregroundGrid;
	private GridGroup backgroundGrid;
	
	// Prefabs
	private ApplePrefab applePrefab;
	
	// Timing
	private float timeUntilNextSpawn;
	
	private static final Random random = new Random();
	
	// Constants
	private static final float MIN_TIME_BETWEEN_SPAWNS = 3;
	private static final float MAX_TIME_BETWEEN_SPAWNS = 7;
	
	public static final int CELL_SIZE = 40;
	public static final int SCALE = 1;
	
	public static final int GRID_WIDTH = 20;
	public static final int GRID_HEIGHT = 20;
	
	public static final int WORLD_WIDTH = CELL_SIZE * GRID_WIDTH * SCALE;
	public static final int WORLD_HEIGHT = CELL_SIZE * GRID_HEIGHT * SCALE;
	
	@Override
	protected void load() {
		super.load();
		
		// Create prefabs
		PlayerHeadPrefab playerHeadPrefab = new PlayerHeadPrefab();
		BackgroundPrefab backgroundPrefab = new BackgroundPrefab();
		applePrefab = new ApplePrefab();
		
		// Add systems to world
		world.addSystem(new PlayerInput());
		world.addSystem(new PlayerHeadMover()); 
		
		// Add entities to world
		backgroundGrid = world.addEntity(new GridPrefab(CELL_SIZE)).getComponent(GridGroup.class);
		foregroundGrid = world.addEntity(new GridPrefab(CELL_SIZE)).getComponent(GridGroup.class);
		
		for (int x = GRID_WIDTH / -2; x <= GRID_WIDTH / 2; x++) {
			for (int y = GRID_WIDTH / -2; y <= GRID_HEIGHT / 2; y++) {
				backgroundGrid.addTile(backgroundPrefab, x, y);
			}
		}
		
		foregroundGrid.addTile(playerHeadPrefab);
		
		timeUntilNextSpawn = MIN_TIME_BETWEEN_SPAWNS;
	}
	
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		super.update(deltaTime);
		
		timeUntilNextSpawn -= deltaTime;
		if (timeUntilNextSpawn <= 0) {
			int x = random.nextInt(GRID_WIDTH) - GRID_WIDTH / 2;
			int y = random.nextInt(GRID_HEIGHT) - GRID_WIDTH / 2;
			
			foregroundGrid.addTile(applePrefab.instantiate(world, x, y));
			
			timeUntilNextSpawn += random.nextFloat(MIN_TIME_BETWEEN_SPAWNS, MAX_TIME_BETWEEN_SPAWNS);
		}
	}
}
