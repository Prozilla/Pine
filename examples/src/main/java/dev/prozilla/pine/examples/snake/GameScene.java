package dev.prozilla.pine.examples.snake;

import dev.prozilla.pine.core.Scene;
import dev.prozilla.pine.examples.snake.entity.PlayerHeadPrefab;
import dev.prozilla.pine.examples.snake.system.PlayerHeadMover;
import dev.prozilla.pine.examples.snake.system.PlayerInput;

public class GameScene extends Scene {
	
	public static final int CELL_SIZE = 40;
	public static final int SCALE = 2;
	
	public static final int GRID_WIDTH = 10;
	public static final int GRID_HEIGHT = 10;
	
	public static final int WORLD_WIDTH = CELL_SIZE * GRID_WIDTH * SCALE;
	public static final int WORLD_HEIGHT = CELL_SIZE * GRID_HEIGHT * SCALE;
	
	@Override
	protected void load() {
		super.load();
		
		// Create prefabs
		PlayerHeadPrefab playerHeadPrefab = new PlayerHeadPrefab();
		
		// Add systems to world
		world.addSystem(new PlayerInput());
		world.addSystem(new PlayerHeadMover());
		
		// Add entities to world
		world.addEntity(playerHeadPrefab);
	}
}
