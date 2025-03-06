package dev.prozilla.pine.examples.snake.entity;

import dev.prozilla.pine.common.random.property.RandomFloatProperty;
import dev.prozilla.pine.common.random.property.RandomIntProperty;
import dev.prozilla.pine.common.random.property.RandomVector2fProperty;
import dev.prozilla.pine.core.component.particle.ParticleBurstEmitter;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.particle.ParticleBurstEmitterPrefab;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

public class AppleParticleEmitterPrefab extends ParticleBurstEmitterPrefab {
	
	private static final float VELOCITY = 0.75f;
	
	public AppleParticleEmitterPrefab() {
		super("snake/apple_parts.png");
		setCount(new RandomIntProperty(3, 6));
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ParticleBurstEmitter particleEmitter = entity.getComponent(ParticleBurstEmitter.class);
		ParticlePrefab particlePrefab = particleEmitter.particlePrefab;
		particlePrefab.setAnimateSprite(false);
		particlePrefab.setFrameCount(4);
		particlePrefab.setInitialFrame(new RandomIntProperty(0, 4));
		particlePrefab.setVelocity(new RandomVector2fProperty(-VELOCITY, VELOCITY, -VELOCITY, VELOCITY));
		particlePrefab.setLifetime(new RandomFloatProperty(0.35f, 0.6f));
		particlePrefab.setScale(new RandomFloatProperty(0.75f, 1));
	}
}
