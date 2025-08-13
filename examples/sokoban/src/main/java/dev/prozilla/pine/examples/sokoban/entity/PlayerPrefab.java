package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;

public class PlayerPrefab extends TilePrefab {
	
	protected int index;
	
	public PlayerPrefab() {
		super("images/player/player_23.png");
		setTag(EntityTag.PLAYER);
		index = 0;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new PlayerData(index));
		
		index++;
	}
}
