package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.core.component.particle.ParticleFlowEmitter;
import dev.prozilla.pine.core.entity.Entity;

public class ParticleFlowEmitterPrefab extends ParticleEmitterPrefab {
	
	protected VariableProperty<Float> spawnDelay;
	protected boolean applyTimeScale;
	
	public ParticleFlowEmitterPrefab(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public ParticleFlowEmitterPrefab(TextureBase texture) {
		this(new ParticlePrefab(texture, ParticlePrefab.DEFAULT_LIFETIME));
	}
	
	public ParticleFlowEmitterPrefab(ParticlePrefab particlePrefab) {
		super(particlePrefab, ParticleFlowEmitter.DEFAULT_COUNT);
		spawnDelay = ParticleFlowEmitter.DEFAULT_SPAWN_DELAY;
		applyTimeScale = ParticleFlowEmitter.APPLY_TIME_SCALE_DEFAULT;
	}
	
	public void setSpawnDelay(VariableProperty<Float> spawnDelay) {
		this.spawnDelay = spawnDelay;
	}
	
	public void setApplyTimeScale(boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
	}
	
	@Override
	protected void apply(Entity entity) {
		ParticleFlowEmitter particleFlowEmitter = entity.addComponent(new ParticleFlowEmitter(particlePrefab, spawnDelay, count));
		particleFlowEmitter.applyTimeScale = applyTimeScale;
	}
}
