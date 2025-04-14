package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.core.component.particle.ParticleBurstEmitter;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.snake.EntityTag;
import dev.prozilla.pine.examples.snake.component.AppleData;

public class ApplePrefab extends TilePrefab {
	
	protected ParticleBurstEmitter particleEmitter;
	
	public ApplePrefab() {
		super("snake/apple.png");
		setName("Apple");
		setTag(EntityTag.APPLE_TAG);
	}
	
	public void setParticleEmitter(ParticleBurstEmitter particleEmitter) {
		this.particleEmitter = particleEmitter;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		AppleData appleData = entity.addComponent(new AppleData());
		appleData.particleEmitter = particleEmitter;
	}
}
