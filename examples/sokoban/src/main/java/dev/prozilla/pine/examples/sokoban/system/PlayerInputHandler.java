package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;

public class PlayerInputHandler extends InputSystem {
	
	public PlayerInputHandler() {
		super(PlayerData.class, TileRenderer.class, AudioEffectPlayer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		TileRenderer tileRenderer = chunk.getComponent(TileRenderer.class);
		AudioEffectPlayer audioEffectPlayer = chunk.getComponent(AudioEffectPlayer.class);
		
		GamepadInput gamepad = input.getGamepad();
		
		if ((input.getKey(Key.L_CONTROL) && input.getKeyDown(Key.Z)) || input.getMouseButtonDown(MouseButton.EXTRA_0) || gamepad.getButtonDown(GamepadButton.B)) {
			if (input.getKey(Key.L_SHIFT)) {
				playerData.history.redo();
			} else {
				playerData.history.undo();
			}
		} else if (input.getAnyKey(playerData.index != 0 ? Key.DOWN_ARROW : Key.S) || gamepad.getAxis(GamepadAxis.LEFT_Y) > PlayerData.JOYSTICK_THRESHOLD) {
			playerData.moveInDirection(Direction.DOWN, tileRenderer, audioEffectPlayer);
		} else if (input.getAnyKey(playerData.index != 0 ? Key.UP_ARROW : Key.W) || gamepad.getAxis(GamepadAxis.LEFT_Y) < -PlayerData.JOYSTICK_THRESHOLD) {
			playerData.moveInDirection(Direction.UP, tileRenderer, audioEffectPlayer);
		} else if (input.getAnyKey(playerData.index != 0 ? Key.LEFT_ARROW : Key.A) || gamepad.getAxis(GamepadAxis.LEFT_X) < -PlayerData.JOYSTICK_THRESHOLD) {
			playerData.moveInDirection(Direction.LEFT, tileRenderer, audioEffectPlayer);
		} else if (input.getAnyKey(playerData.index != 0 ? Key.RIGHT_ARROW : Key.D) || gamepad.getAxis(GamepadAxis.LEFT_X) > PlayerData.JOYSTICK_THRESHOLD) {
			playerData.moveInDirection(Direction.RIGHT, tileRenderer, audioEffectPlayer);
		}
	}
}
