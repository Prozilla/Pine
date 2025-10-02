package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.core.entity.prefab.Prefab;

public abstract class ParticleEmitterPrefab extends Prefab {
	
	protected Property<Integer> count;
	protected final ParticlePrefab particlePrefab;
	
	public ParticleEmitterPrefab(ParticlePrefab particlePrefab, Property<Integer> count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
	public void setCount(Property<Integer> count) {
		this.count = count;
	}
	
}
