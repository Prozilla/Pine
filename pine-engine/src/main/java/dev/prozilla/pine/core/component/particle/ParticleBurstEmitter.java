package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.random.property.FixedProperty;
import dev.prozilla.pine.common.random.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

public class ParticleBurstEmitter extends Component {
	
	public VariableProperty<Integer> count;
	public ParticlePrefab particlePrefab;
	
	public static final VariableProperty<Float> DEFAULT_LIFETIME = new FixedProperty<>(5f);
	public static final VariableProperty<Integer> DEFAULT_COUNT = new FixedProperty<>(5);
	
	public ParticleBurstEmitter(Texture texture) {
		this(texture, DEFAULT_LIFETIME);
	}
	
	public ParticleBurstEmitter(Texture texture, VariableProperty<Float> lifetime) {
		this(texture, lifetime, DEFAULT_COUNT);
	}
	
	public ParticleBurstEmitter(Texture texture, VariableProperty<Float> lifetime, VariableProperty<Integer> count) {
		this(new ParticlePrefab(texture, lifetime), count);
	}
	
	public ParticleBurstEmitter(ParticlePrefab particlePrefab, VariableProperty<Integer> count) {
		this.particlePrefab = particlePrefab;
		this.count = count;
	}
	
	public void emit() {
		emit(0, 0);
	}
	
	public void emit(Vector2f position) {
		emit(position.x, position.y);
	}
	
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
