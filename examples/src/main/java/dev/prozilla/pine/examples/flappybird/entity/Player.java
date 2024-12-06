package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Sprite;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class Player extends Sprite {
	
	public PlayerData playerData;
	
	public Player(World world) {
		super(world, "flappybird/bird.png");
		
		tag = EntityTag.PLAYER_TAG;
		
		playerData = new PlayerData();
		addComponent(playerData);
	}
	
	@Override
	public String getName() {
		return getName("Player");
	}
}
