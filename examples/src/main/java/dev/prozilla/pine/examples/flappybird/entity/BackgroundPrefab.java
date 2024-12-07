package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.SpritePrefab;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

public class BackgroundPrefab extends SpritePrefab {
	
	protected int index;
	
	public BackgroundPrefab() {
		super( "flappybird/background.png");
		
		index = 0;
	}
	
	public Entity instantiate(World world, int index) {
		return instantiate(world, 0, 0, index);
	}
	
	public Entity instantiate(World world, float x, float y, int index) {
		this.index = index;
		return super.instantiate(world, x, y);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new BackgroundData(index));
	}
}
