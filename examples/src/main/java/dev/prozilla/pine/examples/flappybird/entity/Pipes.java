package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

import java.util.Random;

public class Pipes extends Entity {
	
	public PipesData pipesData;
	
	public Pipes(World world) {
		super(world);
		
		// Add pipes
		Pipe bottomPipe = (Pipe)addChild(new Pipe(world, false));
		Pipe topPipe = (Pipe)addChild(new Pipe(world, true));
		
		pipesData = new PipesData(bottomPipe, topPipe);
		addComponent(pipesData);
	}
	
	@Override
	public String getName() {
		return getName("Pipes");
	}
}
