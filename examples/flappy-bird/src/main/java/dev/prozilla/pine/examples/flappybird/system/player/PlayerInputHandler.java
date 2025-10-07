package dev.prozilla.pine.examples.flappybird.system.player;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

/**
 * Handles input for the player.
 */
public class PlayerInputHandler extends InputSystem {
	
	public PlayerInputHandler() {
		super(Transform.class, SpriteRenderer.class, PlayerData.class, AudioEffectPlayer.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Input input, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		AudioEffectPlayer audioEffectPlayer = chunk.getComponent(AudioEffectPlayer.class);
		
		if (!playerData.gameScene.gameOver) {
			// Jump
			if (playerData.jumpButton.isDown(input)) {
				playerData.velocity = playerData.jumpVelocity.getValue();
				audioEffectPlayer.playRandom();
			}
			// Pause
			if (playerData.pauseButton.isDown(input)) {
				application.togglePause();
			}
		}
	}
}
