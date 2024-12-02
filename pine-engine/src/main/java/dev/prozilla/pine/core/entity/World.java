package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.state.Scene;

public class World extends Entity {
	
	public World(Game game, Scene scene) {
		super(game);
		
		this.scene = scene;
	}
	
	@Override
	public World getWorld() {
		return this;
	}
	
	@Override
	public String getName() {
		return getName("World");
	}
}
