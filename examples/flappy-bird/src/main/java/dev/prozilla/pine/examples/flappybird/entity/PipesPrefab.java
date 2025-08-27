package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.random.RandomFloatProperty;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.examples.flappybird.component.PipesData;

public class PipesPrefab extends Prefab {
	
	public PipesPrefab() {
		setName("Pipes");
	}
	
	@Override
	protected void apply(Entity entity) {
		PipePrefab pipePrefab = new PipePrefab();
		
		// Add pipes
		Entity bottomPipe = entity.addChild(pipePrefab.instantiate(entity.getWorld(), false));
		Entity topPipe = entity.addChild(pipePrefab.instantiate(entity.getWorld(), true));
		
		entity.addComponent(new PipesData(bottomPipe, topPipe));
		
		// Add sound effect
		AudioEffectPlayer audioEffectPlayer = new AudioEffectPlayer(AssetPools.audioSources.loadAll("audio/pipe-impact.ogg"));
		audioEffectPlayer.setVolume(new RandomFloatProperty(0.5f, 0.75f));
		audioEffectPlayer.setPitch(new RandomFloatProperty(0.75f, 1.25f));
		entity.addComponent(audioEffectPlayer);
	}
}
