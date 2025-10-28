package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

public class BackgroundPrefab extends SpritePrefab {
	
	protected int index;
	
	public BackgroundPrefab() {
		super("flappybird/background.png");
		setName("Background");
		setScale(new Vector2f(1.01f, 1.01f));
		
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
