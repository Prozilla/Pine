package dev.prozilla.pine.core.system.standard.particle;

import dev.prozilla.pine.core.component.particle.ParticleFlowEmitter;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ParticleFlowUpdater extends UpdateSystem {
	
	public ParticleFlowUpdater() {
		super(ParticleFlowEmitter.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ParticleFlowEmitter particleFlowEmitter = chunk.getComponent(ParticleFlowEmitter.class);
		
		if (!particleFlowEmitter.isSpawning) {
			return;
		}
		
		if (particleFlowEmitter.applyTimeScale) {
			deltaTime = application.getTimer().getScaledDeltaTime();
		}
		
		particleFlowEmitter.timeUntilSpawn -= deltaTime;
		
		if (particleFlowEmitter.timeUntilSpawn <= 0) {
			int spawnCount = particleFlowEmitter.count.getValue();
			
			if (spawnCount <= 0) {
				return;
			}
			
			for (int i = 0; i < spawnCount; i++) {
				Entity particle = particleFlowEmitter.particlePrefab.instantiate(world, particleFlowEmitter.origin.x, particleFlowEmitter.origin.y);
				particleFlowEmitter.getEntity().addChild(particle);
			}
			
			particleFlowEmitter.timeUntilSpawn = particleFlowEmitter.spawnDelay.getValue();
		}
	}
}
