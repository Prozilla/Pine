package dev.prozilla.pine.examples.flappybird.system.player;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

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
		
		// Create deserializer and properties
		playerData.readData(playerData.gameScene.dataDirectory, "player.json", PlayerData.Data.class);
		
		// Set player properties
		playerData.animationFrame = 0;
		playerData.age = 0;
		playerData.velocity = playerData.jumpVelocity.get();
		
		transform.setPosition(PlayerData.POSITION_X, 0);
		
		// Set sprite properties
		spriteRenderer.scale.set(PlayerData.SCALE);
		spriteRenderer.rotation = 0;
	}
}
