package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.core.entity.prefab.Prefab;

public abstract class ParticleEmitterPrefab extends Prefab {
	
	protected IntProperty count;
	protected final ParticlePrefab particlePrefab;
	
	public ParticleEmitterPrefab(ParticlePrefab particlePrefab, IntProperty count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
	public void setCount(IntProperty count) {
		this.count = count;
	}
	
}
