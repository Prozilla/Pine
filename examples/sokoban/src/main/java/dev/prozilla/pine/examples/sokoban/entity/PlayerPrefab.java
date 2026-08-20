package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.random.RandomFloatProperty;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.component.History;
import dev.prozilla.pine.examples.sokoban.component.NetworkPlayer;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;

public class PlayerPrefab extends TilePrefab {
	
	protected int index;
	
	public PlayerPrefab() {
		this(0);
	}
	
	public PlayerPrefab(int index) {
		super("images/player/player_23.png");
		setTag(EntityTag.PLAYER);
		this.index = index;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new PlayerData(index));
		entity.addComponent(new NetworkPlayer(index));
		entity.addComponent(new History(coordinate.clone()));
		
		AudioEffectPlayer audioEffectPlayer = new AudioEffectPlayer(AssetPools.audioSources.loadAll("audio/pushing-crate.ogg"));
		audioEffectPlayer.setVolume(new RandomFloatProperty(0.85f, 1.15f));
		audioEffectPlayer.setPitch(new RandomFloatProperty(0.4f, 0.6f));
		entity.addComponent(audioEffectPlayer);
		
		index++;
	}
}
