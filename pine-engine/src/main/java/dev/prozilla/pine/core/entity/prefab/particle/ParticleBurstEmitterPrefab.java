package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.random.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.particle.ParticleBurstEmitter;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;

public class ParticleBurstEmitterPrefab extends Prefab {
	
	protected VariableProperty<Integer> count;
	protected ParticlePrefab particlePrefab;
	
	public ParticleBurstEmitterPrefab(String texturePath) {
		this(ResourcePool.loadTexture(texturePath));
	}
	
	public ParticleBurstEmitterPrefab(Texture texture) {
		this(new ParticlePrefab(texture, ParticleBurstEmitter.DEFAULT_LIFETIME));
	}
	
	public ParticleBurstEmitterPrefab(ParticlePrefab particlePrefab) {
		this.particlePrefab = particlePrefab;
		count = ParticleBurstEmitter.DEFAULT_COUNT;
	}
	
	public void setCount(VariableProperty<Integer> count) {
		this.count = count;
	}
	
	@Override
	protected void apply(Entity entity) {
		entity.addComponent(new ParticleBurstEmitter(particlePrefab, count));
	}
}
