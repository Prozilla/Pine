package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.physics.collision.RectCollider;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.flappybird.component.PipeData;

public class PipePrefab extends SpritePrefab {
	
	protected boolean top;
	
	public PipePrefab() {
		super("flappybird/pipe.png");
		setName("Pipe");
	}
	
	public Entity instantiate(Scene scene, boolean top) {
		return instantiate(scene, 0, 0, top);
	}
	
	public Entity instantiate(Scene scene, float x, float y, boolean top) {
		this.top = top;
		return instantiate(scene, x, y, 0);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		RectCollider collider = entity.addComponent(new RectCollider(new Vector2f(PipeData.WIDTH, PipeData.HEIGHT)));
		
		entity.addComponent(new PipeData(top, collider));
	}
}
