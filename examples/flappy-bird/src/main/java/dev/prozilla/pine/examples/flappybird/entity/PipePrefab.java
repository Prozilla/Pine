package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.physics.collision.RectCollider;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.examples.flappybird.component.PipeData;

public class PipePrefab extends SpritePrefab {
	
	protected boolean top;
	
	public PipePrefab() {
		super("flappybird/pipe.png");
		setName("Pipe");
	}
	
	public Entity instantiate(World world, boolean top) {
		return instantiate(world, 0, 0, top);
	}
	
	public Entity instantiate(World world, float x, float y, boolean top) {
		this.top = top;
		return instantiate(world, x, y);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		RectCollider collider = entity.addComponent(new RectCollider(new Vector2f(PipeData.WIDTH, PipeData.HEIGHT)));
		
		entity.addComponent(new PipeData(top, collider));
	}
}
