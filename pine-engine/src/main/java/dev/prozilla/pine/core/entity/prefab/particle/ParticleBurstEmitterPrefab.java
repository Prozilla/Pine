package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.component.particle.ParticleBurstEmitter;
import dev.prozilla.pine.core.entity.Entity;

public class ParticleBurstEmitterPrefab extends ParticleEmitterPrefab {
	
	public ParticleBurstEmitterPrefab(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public ParticleBurstEmitterPrefab(TextureBase texture) {
		this(new ParticlePrefab(texture, ParticlePrefab.DEFAULT_LIFETIME));
	}
	
	public ParticleBurstEmitterPrefab(ParticlePrefab particlePrefab) {
		super(particlePrefab, ParticleBurstEmitter.DEFAULT_COUNT);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new ParticleBurstEmitter(particlePrefab, count));
	}
}
