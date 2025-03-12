package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.random.property.VariableProperty;
import dev.prozilla.pine.core.entity.prefab.Prefab;

public abstract class ParticleEmitterPrefab extends Prefab {
	
	protected VariableProperty<Integer> count;
	protected final ParticlePrefab particlePrefab;
	
	public ParticleEmitterPrefab(ParticlePrefab particlePrefab, VariableProperty<Integer> count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
	public void setCount(VariableProperty<Integer> count) {
		this.count = count;
	}
	
}
