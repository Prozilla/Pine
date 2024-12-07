package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.SpritePrefab;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class PlayerPrefab extends SpritePrefab {
	
	public PlayerPrefab() {
		super("flappybird/bird.png");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.tag = EntityTag.PLAYER_TAG;
		entity.addComponent(new PlayerData());
	}
}
