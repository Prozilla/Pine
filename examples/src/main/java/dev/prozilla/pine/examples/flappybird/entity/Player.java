package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.Sprite;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class Player extends Sprite {
	
	public PlayerData playerData;
	
	public static final ComponentCollector collector = new ComponentCollector(Transform.class, SpriteRenderer.class, PlayerData.class);
	
	public Player(World world) {
		super(world, "flappybird/bird.png");
		
		playerData = new PlayerData();
		addComponent(playerData);
	}
	
	@Override
	public String getName() {
		return getName("Player");
	}
}
