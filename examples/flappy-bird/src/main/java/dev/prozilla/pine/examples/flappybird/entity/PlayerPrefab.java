package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.random.RandomFloatProperty;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.physics.collision.CircleCollider;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class PlayerPrefab extends SpritePrefab {
	
	public PlayerPrefab() {
		super("flappybird/bird.png");
		setName("Player");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		CircleCollider collider = entity.addComponent(new CircleCollider(PlayerData.COLLIDER_RADIUS, PlayerData.COLLIDER_OFFSET));
		
		entity.tag = EntityTag.PLAYER_TAG;
		entity.addComponent(new PlayerData(collider));
		
		AudioEffectPlayer audioEffectPlayer = new AudioEffectPlayer(AssetPools.audioSources.loadAll("audio/swosh-05.ogg"));
		audioEffectPlayer.setVolume(new RandomFloatProperty(0.15f, 0.35f));
		audioEffectPlayer.setPitch(new RandomFloatProperty(0.75f, 1.25f));
		entity.addComponent(audioEffectPlayer);
	}
}
