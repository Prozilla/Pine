package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;
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
		
		GamepadInput gamepad = input.getGamepad();
		
		// Change player direction based on input and previous direction
		int newDirection = -1;
		if (input.getAnyKey(Key.UP_ARROW, Key.W) || gamepad.getAxis(GamepadAxis.LEFT_Y) < -PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 0;
		} else if (input.getAnyKey(Key.LEFT_ARROW, Key.A) || gamepad.getAxis(GamepadAxis.LEFT_X) < -PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 1;
		} else if (input.getAnyKey(Key.DOWN_ARROW, Key.S) || gamepad.getAxis(GamepadAxis.LEFT_Y) > PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 2;
		} else if (input.getAnyKey(Key.RIGHT_ARROW, Key.D) || gamepad.getAxis(GamepadAxis.LEFT_X) > PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 3;
		}
		
		if (newDirection != -1 && (playerData.previousMoveDirection - newDirection) % 2 != 0) {
			playerData.direction = newDirection;
		}
	}
}
