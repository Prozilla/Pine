package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

public class BackgroundPrefab extends SpritePrefab {
	
	protected int index;
	
	public BackgroundPrefab() {
		super(AssetPools.textures.load("flappybird/background.png", Texture.Wrap.MIRRORED_REPEAT));
		setName("Background");
		setScale(new Vector2f(1.1f, 1.1f));
		
		index = 0;
	}
	
	public Entity instantiate(Scene scene, int index) {
		return instantiate(scene, 0, 0, index);
	}
	
	public Entity instantiate(Scene scene, float x, float y, int index) {
		this.index = index;
		return super.instantiate(scene, x, y, 0);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new BackgroundData(index));
	}
}
