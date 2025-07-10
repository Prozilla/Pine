package dev.prozilla.pine.examples.sokoban.util;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;

public class PlayerMoveAction extends TileMoveAction {

	private final PlayerData playerData;
	private final Direction direction;
	
	public PlayerMoveAction(TileRenderer tile, PlayerData playerData) {
		super(tile, playerData.direction.x, playerData.direction.y);
		this.playerData = playerData;
		direction = playerData.direction;
	}
	
	@Override
	public void undo() {
		super.undo();
		playerData.direction = direction;
		playerData.timeUntilMoveCompletes = 0;
		playerData.canMove = false;
	}
	
	@Override
	public void redo() {
		super.redo();
		playerData.direction = direction;
		playerData.timeUntilMoveCompletes = 0;
		playerData.canMove = false;
	}
	
}
