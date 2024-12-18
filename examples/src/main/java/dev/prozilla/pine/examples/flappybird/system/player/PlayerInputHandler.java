package dev.prozilla.pine.examples.flappybird.system.player;

import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

/**
 * Handles input for the player.
 */
public class PlayerInputHandler extends InputSystem {
	
	public PlayerInputHandler() {
		super(Transform.class, SpriteRenderer.class, PlayerData.class);
	}
	
	@Override
	public void process(EntityMatch match, Input input, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		if (!playerData.gameScene.gameOver) {
			// Jump
			if (input.getKeyDown(Key.SPACE) || input.getMouseButtonDown(MouseButton.LEFT)) {
				playerData.velocity = PlayerData.JUMP_VELOCITY;
			}
		}
	}
}
