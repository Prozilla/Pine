package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.examples.flappybird.component.GroundData;

public class GroundPrefab extends SpritePrefab {
	
	protected int index;
	
	public GroundPrefab() {
		super("flappybird/base.png");
		setName("Ground");
		
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
		
		entity.addComponent(new GroundData(index));
	}
}

