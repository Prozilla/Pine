package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;

public class PlayerInputHandler extends InputSystem {
	
	public PlayerInputHandler() {
		super(PlayerData.class, TileRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		TileRenderer tileRenderer = chunk.getComponent(TileRenderer.class);
		
		if (input.getKey(Key.L_CONTROL) && input.getKeyDown(Key.Z)) {
			if (input.getKey(Key.L_SHIFT)) {
				playerData.history.redo();
			} else {
				playerData.history.undo();
			}
		} else if (input.getAnyKey(Key.DOWN_ARROW, Key.S)) {
			playerData.moveInDirection(Direction.DOWN, tileRenderer);
		} else if (input.getAnyKey(Key.UP_ARROW, Key.W)) {
			playerData.moveInDirection(Direction.UP, tileRenderer);
		} else if (input.getAnyKey(Key.LEFT_ARROW, Key.A)) {
			playerData.moveInDirection(Direction.LEFT, tileRenderer);
		} else if (input.getAnyKey(Key.RIGHT_ARROW, Key.D)) {
			playerData.moveInDirection(Direction.RIGHT, tileRenderer);
		}
	}
}
