package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

/**
 * Spawns a given amount of particles in bursts.
 */
public class ParticleBurstEmitter extends ParticleEmitter {
	
	public ParticleBurstEmitter(ParticlePrefab particlePrefab) {
		this(particlePrefab, DEFAULT_COUNT);
	}
	
	public ParticleBurstEmitter(ParticlePrefab particlePrefab, VariableProperty<Integer> count) {
		super(particlePrefab, count);
	}
	
	/**
	 * Spawns particles at (0, 0)
	 */
	public void emit() {
		emit(0, 0);
	}
	
	/**
	 * Spawns particles at a given position.
	 */
	public void emit(Vector2f position) {
		emit(position.x, position.y);
	}
	
	/**
	 * Spawns particles at a given position.
	 */
	public void emit(float x, float y) {
		int burstCount = count.getValue();
		
		if (burstCount <= 0) {
			return;
		}
		
		for (int i = 0; i < burstCount; i++) {
			Entity particle = particlePrefab.instantiate(getWorld(), x, y);
			entity.addChild(particle);
		}
	}
	
}
