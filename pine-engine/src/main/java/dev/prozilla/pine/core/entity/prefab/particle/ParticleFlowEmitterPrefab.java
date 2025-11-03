package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.core.component.particle.ParticleFlowEmitter;
import dev.prozilla.pine.core.entity.Entity;

public class ParticleFlowEmitterPrefab extends ParticleEmitterPrefab {
	
	protected FloatProperty spawnDelay;
	protected boolean applyTimeScale;
	
	public ParticleFlowEmitterPrefab(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public ParticleFlowEmitterPrefab(TextureAsset texture) {
		this(new ParticlePrefab(texture, ParticlePrefab.DEFAULT_LIFETIME));
	}
	
	public ParticleFlowEmitterPrefab(ParticlePrefab particlePrefab) {
		super(particlePrefab, ParticleFlowEmitter.DEFAULT_COUNT);
		spawnDelay = ParticleFlowEmitter.DEFAULT_SPAWN_DELAY;
		applyTimeScale = ParticleFlowEmitter.APPLY_TIME_SCALE_DEFAULT;
	}
	
	public void setSpawnDelay(FloatProperty spawnDelay) {
		this.spawnDelay = spawnDelay;
	}
	
	public void setApplyTimeScale(boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ParticleFlowEmitter particleFlowEmitter = entity.addComponent(new ParticleFlowEmitter(particlePrefab, spawnDelay, count));
		particleFlowEmitter.applyTimeScale = applyTimeScale;
	}
}
