package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Sprite;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;

public class Pipe extends Sprite {
	
	public PipeData pipeData;
	
	public Pipe(World world, boolean top) {
		super(world, "flappybird/pipe.png");
		
		pipeData = new PipeData(top);
		addComponent(pipeData);
	}
	
	@Override
	public String getName() {
		return getName("Pipe");
	}
}
