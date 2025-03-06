package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.random.property.FixedProperty;
import dev.prozilla.pine.common.random.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

public class ParticleFlowEmitter extends ParticleEmitter {
	
	public VariableProperty<Float> spawnDelay;
	public float timeUntilSpawn;
	public boolean isSpawning;
	public Vector2f origin;
	
	public static final VariableProperty<Float> DEFAULT_SPAWN_DELAY = new FixedProperty<>(1f);
	
	public ParticleFlowEmitter(Texture texture) {
		this(texture, DEFAULT_LIFETIME);
	}
	
	public ParticleFlowEmitter(Texture texture, VariableProperty<Float> lifetime) {
		this(texture, lifetime, DEFAULT_SPAWN_DELAY);
	}
	
	public ParticleFlowEmitter(Texture texture, VariableProperty<Float> lifetime, VariableProperty<Float> spawnDelay) {
		this(texture, lifetime, spawnDelay, DEFAULT_COUNT);
	}
	
	public ParticleFlowEmitter(Texture texture, VariableProperty<Float> lifetime, VariableProperty<Float> spawnDelay, VariableProperty<Integer> count) {
		this(new ParticlePrefab(texture, lifetime), spawnDelay, count);
	}
	
	public ParticleFlowEmitter(ParticlePrefab particlePrefab, VariableProperty<Float> spawnDelay, VariableProperty<Integer> count) {
		super(particlePrefab, count);
		this.spawnDelay = spawnDelay;
		timeUntilSpawn = 0;
		isSpawning = true;
		origin = new Vector2f();
	}
	
	public void start() {
		timeUntilSpawn = spawnDelay.getValue();
		isSpawning = true;
	}
	
	public void stop() {
		isSpawning = false;
	}
	
}
