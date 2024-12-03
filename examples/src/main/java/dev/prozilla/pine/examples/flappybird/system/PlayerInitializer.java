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
		forEach(componentGroup -> {
			Transform transform = componentGroup.getComponent(Transform.class);
			SpriteRenderer spriteRenderer = componentGroup.getComponent(SpriteRenderer.class);
			PlayerData playerData = componentGroup.getComponent(PlayerData.class);
			
			// Store reference to scene
			playerData.gameScene = (GameScene)world.scene;
			
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
