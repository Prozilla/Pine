package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.fixed.FixedIntProperty;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

/**
 * Base class for particle emitter components.
 */
public abstract class ParticleEmitter extends Component {
	
	/**
	 * The prefab used to spawn particles.
	 */
	public ParticlePrefab particlePrefab;
	/**
	 * The amount of particles to spawn.
	 */
	public IntProperty count;
	
	public static final FixedIntProperty DEFAULT_COUNT = new FixedIntProperty(5);
	
	public ParticleEmitter(ParticlePrefab particlePrefab, IntProperty count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
}
