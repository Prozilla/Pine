package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.random.property.FixedProperty;
import dev.prozilla.pine.common.random.property.VariableProperty;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

public abstract class ParticleEmitter extends Component {
	
	public ParticlePrefab particlePrefab;
	public VariableProperty<Integer> count;
	
	public static final VariableProperty<Float> DEFAULT_LIFETIME = new FixedProperty<>(5f);
	public static final VariableProperty<Integer> DEFAULT_COUNT = new FixedProperty<>(5);
	
	public ParticleEmitter(ParticlePrefab particlePrefab, VariableProperty<Integer> count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
}
