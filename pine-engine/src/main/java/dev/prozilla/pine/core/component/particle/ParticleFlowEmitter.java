package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.core.entity.prefab.particle.ParticlePrefab;

/**
 * Continuously spawns particles
 */
public class ParticleFlowEmitter extends ParticleEmitter {
	
	public FloatProperty spawnDelay;
	public float timeUntilSpawn;
	public boolean isSpawning;
	public Vector2f origin;
	public boolean applyTimeScale;
	
	public static final FixedFloatProperty DEFAULT_SPAWN_DELAY = new FixedFloatProperty(1f);
	public static final boolean APPLY_TIME_SCALE_DEFAULT = true;
	
	public ParticleFlowEmitter(ParticlePrefab particlePrefab) {
		this(particlePrefab, DEFAULT_SPAWN_DELAY);
	}
	
	public ParticleFlowEmitter(ParticlePrefab particlePrefab, FloatProperty spawnDelay) {
		this(particlePrefab, spawnDelay, DEFAULT_COUNT);
	}
	
	public ParticleFlowEmitter(ParticlePrefab particlePrefab, FloatProperty spawnDelay, IntProperty count) {
		super(particlePrefab, count);
		this.spawnDelay = spawnDelay;
		timeUntilSpawn = 0;
		isSpawning = false;
		origin = new Vector2f();
		applyTimeScale = APPLY_TIME_SCALE_DEFAULT;
	}
	
	/**
	 * Start timer for spawning particles.
	 */
	public void start() {
		timeUntilSpawn = spawnDelay.get();
		isSpawning = true;
	}
	
	/**
	 * Start spawning particles immediately.
	 */
	public void startImmediate() {
		timeUntilSpawn = 0;
		isSpawning = true;
	}
	
	/**
	 * Stop spawning particles.
	 */
	public void stop() {
		isSpawning = false;
	}
	
}
