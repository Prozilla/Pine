package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Gamepad;
import dev.prozilla.pine.core.state.input.GamepadAxis;
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
		if (input.getAnyKey(Key.UP_ARROW, Key.W) || input.getGamepadAxis(Gamepad.ID_0, GamepadAxis.LEFT_Y) < -PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 0;
		} else if (input.getAnyKey(Key.LEFT_ARROW, Key.A) || input.getGamepadAxis(Gamepad.ID_0, GamepadAxis.LEFT_X) < -PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 1;
		} else if (input.getAnyKey(Key.DOWN_ARROW, Key.S) || input.getGamepadAxis(Gamepad.ID_0, GamepadAxis.LEFT_Y) > PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 2;
		} else if (input.getAnyKey(Key.RIGHT_ARROW, Key.D) || input.getGamepadAxis(Gamepad.ID_0, GamepadAxis.LEFT_X) > PlayerData.JOYSTICK_THRESHOLD) {
			newDirection = 3;
		}
		
		if (newDirection != -1 && (playerData.previousMoveDirection - newDirection) % 2 != 0) {
			playerData.direction = newDirection;
		}
	}
}
