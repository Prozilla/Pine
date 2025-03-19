package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.property.FixedProperty;
import dev.prozilla.pine.common.property.VariableProperty;
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
	public VariableProperty<Integer> count;
	
	public static final VariableProperty<Integer> DEFAULT_COUNT = new FixedProperty<>(5);
	
	public ParticleEmitter(ParticlePrefab particlePrefab, VariableProperty<Integer> count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
}
