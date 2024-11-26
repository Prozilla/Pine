package dev.prozilla.pine.core.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.state.Scene;

public class World extends GameObject {
	
	/**
	 * Creates a world object named "World".
	 * @param game Reference to the game
	 */
	public World(Game game, Scene scene) {
		super(game, "World");
		
		this.scene = scene;
	}
	
	@Override
	public World getWorld() {
		return this;
	}
}
