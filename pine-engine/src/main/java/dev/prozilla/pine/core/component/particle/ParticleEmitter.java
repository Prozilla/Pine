package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
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
	public Property<Integer> count;
	
	public static final Property<Integer> DEFAULT_COUNT = new FixedObjectProperty<>(5);
	
	public ParticleEmitter(ParticlePrefab particlePrefab, Property<Integer> count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
}
