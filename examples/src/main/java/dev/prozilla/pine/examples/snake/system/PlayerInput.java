package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.snake.component.PlayerData;

public class PlayerInput extends InputSystem {
	
	public PlayerInput() {
		super(PlayerData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		if (playerData.scene.gameOver) {
			return;
		}
		
		// Change player direction based on input and previous direction
		int newDirection = -1;
		if (input.getKeyDown(Key.UP_ARROW) || input.getKeyDown(Key.W)) {
			newDirection = 0;
		} else if (input.getKeyDown(Key.LEFT_ARROW) || input.getKeyDown(Key.A)) {
			newDirection = 1;
		} else if (input.getKeyDown(Key.DOWN_ARROW) || input.getKeyDown(Key.S)) {
			newDirection = 2;
		} else if (input.getKeyDown(Key.RIGHT_ARROW) || input.getKeyDown(Key.D)) {
			newDirection = 3;
		}
		
		if (newDirection != -1 && (playerData.previousMoveDirection - newDirection) % 2 != 0) {
			playerData.direction = newDirection;
		}
	}
}
