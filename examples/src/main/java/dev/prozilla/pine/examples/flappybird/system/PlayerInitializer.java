package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.component.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
import dev.prozilla.pine.examples.flappybird.entity.Player;

public class PlayerInitializer extends InitSystem {
	
	public PlayerInitializer() {
		super(Player.collector);
	}
	
	@Override
	public void init(long window) {
		forEach(match -> {
			Transform transform = match.getComponent(Transform.class);
			SpriteRenderer spriteRenderer = match.getComponent(SpriteRenderer.class);
			PlayerData playerData = match.getComponent(PlayerData.class);
			
			// Store reference to scene
			playerData.gameScene = (GameScene)scene;
			
			// Set player properties
			playerData.animationFrame = 0;
			playerData.age = 0;
			playerData.velocity = PlayerData.JUMP_VELOCITY;
			
			transform.setPosition(PlayerData.POSITION_X, 0);
			
			// Set sprite properties
			spriteRenderer.scale = 1.5f;
			spriteRenderer.rotation = 0;
		});
	}
}
