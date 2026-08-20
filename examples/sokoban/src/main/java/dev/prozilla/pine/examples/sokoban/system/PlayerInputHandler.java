package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.sokoban.component.*;
import dev.prozilla.pine.examples.sokoban.net.packet.MoveRequestPacket;
import dev.prozilla.pine.examples.sokoban.net.packet.RestartRequestPacket;
import dev.prozilla.pine.examples.sokoban.net.packet.UndoRequestPacket;

public class PlayerInputHandler extends InputSystem {
	
	private final GridGroup foregroundGrid;
	private final NetworkManager network;
	
	public PlayerInputHandler(GridGroup foregroundGrid, NetworkManager network) {
		super(PlayerData.class, TileRenderer.class, AudioEffectPlayer.class, NetworkPlayer.class, History.class);
		this.foregroundGrid = foregroundGrid;
		this.network = network;
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		NetworkPlayer networkPlayer = chunk.getComponent(NetworkPlayer.class);
		
		if (!network.isLocalPlayer(networkPlayer.id)) {
			return;
		}
		
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		if (input.getKeyDown(Key.R) && network.isHost(networkPlayer.id)) {
			network.send(new RestartRequestPacket());
			return;
		}
		
		if (isUndoDown(input)) {
			if (!networkPlayer.awaitingConfirm && playerData.timeUntilMoveCompletes <= 0) {
				network.send(new UndoRequestPacket());
			}
			return;
		}
		
		Direction direction = getMoveDirection(input);
		if (direction == null) {
			return;
		}
		
		if (networkPlayer.awaitingConfirm || playerData.timeUntilMoveCompletes > 0) {
			return;
		}
		
		Move move = playerData.computeMove(foregroundGrid, direction);
		if (move == null) {
			playerData.blockMove(direction);
			return;
		}
		
		playerData.beginMove(move, foregroundGrid);
		networkPlayer.awaitingConfirm = true;
		network.send(new MoveRequestPacket(direction));
	}
	
	private static Direction getMoveDirection(Input input) {
		GamepadInput gamepad = input.getGamepad();
		if (input.getAnyKey(Key.S, Key.DOWN_ARROW) || gamepad.getAxis(GamepadAxis.LEFT_Y) > PlayerData.JOYSTICK_THRESHOLD) {
			return Direction.DOWN;
		} else if (input.getAnyKey(Key.W, Key.UP_ARROW) || gamepad.getAxis(GamepadAxis.LEFT_Y) < -PlayerData.JOYSTICK_THRESHOLD) {
			return Direction.UP;
		} else if (input.getAnyKey(Key.A, Key.LEFT_ARROW) || gamepad.getAxis(GamepadAxis.LEFT_X) < -PlayerData.JOYSTICK_THRESHOLD) {
			return Direction.LEFT;
		} else if (input.getAnyKey(Key.D, Key.RIGHT_ARROW) || gamepad.getAxis(GamepadAxis.LEFT_X) > PlayerData.JOYSTICK_THRESHOLD) {
			return Direction.RIGHT;
		}
		return null;
	}
	
	private static boolean isUndoDown(Input input) {
		return (input.getKey(Key.L_CONTROL) && input.getKeyDown(Key.Z))
			       || input.getMouseButtonDown(MouseButton.EXTRA_0)
			       || input.getGamepad().getButtonDown(GamepadButton.B);
	}
	
}
