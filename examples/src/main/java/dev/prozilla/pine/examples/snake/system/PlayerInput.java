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
		
		// Change player direction based on input and previous direction
		if ((input.getKeyDown(Key.UP_ARROW) || input.getKeyDown(Key.W)) && playerData.previousMoveDirection != 2) {
			playerData.direction = 0;
		} else if ((input.getKeyDown(Key.LEFT_ARROW) || input.getKeyDown(Key.A)) && playerData.previousMoveDirection != 3) {
			playerData.direction = 1;
		} else if ((input.getKeyDown(Key.DOWN_ARROW) || input.getKeyDown(Key.S)) && playerData.previousMoveDirection != 0) {
			playerData.direction = 2;
		} else if ((input.getKeyDown(Key.RIGHT_ARROW) || input.getKeyDown(Key.D)) && playerData.previousMoveDirection != 1) {
			playerData.direction = 3;
		}
	}
}
