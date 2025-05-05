package dev.prozilla.pine.examples.sokoban.util;

public class Command implements Action {
	
	private final TileMoveAction playerMoveAction;
	private final TileMoveAction crateMoveAction;
	
	public Command(TileMoveAction playerMoveAction, TileMoveAction crateMoveAction) {
		this.playerMoveAction = playerMoveAction;
		this.crateMoveAction = crateMoveAction;
	}
	
	@Override
	public void undo() {
		playerMoveAction.undo();
		if (crateMoveAction != null) {
			crateMoveAction.undo();
		}
	}
	
	@Override
	public void redo() {
		if (crateMoveAction != null) {
			crateMoveAction.redo();
		}
		playerMoveAction.redo();
	}
	
}
