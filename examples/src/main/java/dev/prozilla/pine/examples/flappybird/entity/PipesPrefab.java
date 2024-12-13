package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.examples.flappybird.component.PipesData;

public class PipesPrefab extends Prefab {
	
	public PipesPrefab() {
		setName("Pipes");
	}
	
	@Override
	protected void apply(Entity entity) {
		PipePrefab pipePrefab = new PipePrefab();
		
		// Add pipes
		Entity bottomPipe = entity.addChild(pipePrefab.instantiate(entity.getWorld(), false));
		Entity topPipe = entity.addChild(pipePrefab.instantiate(entity.getWorld(), true));
		
		entity.addComponent(new PipesData(bottomPipe, topPipe));
	}
}
