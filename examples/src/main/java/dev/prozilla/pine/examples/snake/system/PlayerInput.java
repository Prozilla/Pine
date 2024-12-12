package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.snake.component.PlayerData;

public class PlayerInput extends InputSystem {
	
	public PlayerInput() {
		super(PlayerData.class);
	}
	
	@Override
	protected void process(EntityMatch match, Input input, float deltaTime) {
		PlayerData playerData = match.getComponent(PlayerData.class);
		
		// Change player direction based on input
		if (input.getKeyDown(Key.UP_ARROW) || input.getKeyDown(Key.W)) {
			playerData.direction = 0;
		} else if (input.getKeyDown(Key.LEFT_ARROW) || input.getKeyDown(Key.A)) {
			playerData.direction = 1;
		} else if (input.getKeyDown(Key.DOWN_ARROW) || input.getKeyDown(Key.S)) {
			playerData.direction = 2;
		} else if (input.getKeyDown(Key.RIGHT_ARROW) || input.getKeyDown(Key.D)) {
			playerData.direction = 3;
		}
	}
}
