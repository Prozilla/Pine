package dev.prozilla.pine.examples.flappybird.system.player;

import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

/**
 * Initializes the player by setting its position and sprite properties.
 */
public class PlayerInitializer extends InitSystem {
	
	public PlayerInitializer() {
		super(Transform.class, SpriteRenderer.class, PlayerData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Transform transform = chunk.getComponent(Transform.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
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
	}
}
