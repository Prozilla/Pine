package dev.prozilla.pine.examples.flappybird.entity;

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
		
		entity.tag = EntityTag.PLAYER_TAG;
		entity.addComponent(new PlayerData());
	}
}
