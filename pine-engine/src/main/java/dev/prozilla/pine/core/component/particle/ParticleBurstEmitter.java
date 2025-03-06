package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.random.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

public class ParticleBurstEmitter extends ParticleEmitter {
	
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
		super(particlePrefab, count);
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
