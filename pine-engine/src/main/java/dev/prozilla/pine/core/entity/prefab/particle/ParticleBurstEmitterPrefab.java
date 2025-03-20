package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.TextureBase;
import dev.prozilla.pine.core.component.particle.ParticleBurstEmitter;
import dev.prozilla.pine.core.entity.Entity;

public class ParticleBurstEmitterPrefab extends ParticleEmitterPrefab {
	
	public ParticleBurstEmitterPrefab(String texturePath) {
		this(ResourcePool.loadTexture(texturePath));
	}
	
	public ParticleBurstEmitterPrefab(TextureBase texture) {
		this(new ParticlePrefab(texture, ParticlePrefab.DEFAULT_LIFETIME));
	}
	
	public ParticleBurstEmitterPrefab(ParticlePrefab particlePrefab) {
		super(particlePrefab, ParticleBurstEmitter.DEFAULT_COUNT);
	}
	
	@Override
	protected void apply(Entity entity) {
		entity.addComponent(new ParticleBurstEmitter(particlePrefab, count));
	}
}
