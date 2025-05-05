package dev.prozilla.pine.examples.sokoban.util;

import dev.prozilla.pine.core.component.sprite.TileRenderer;

public class TileMoveAction implements Action {

	private final TileRenderer tile;
	private final int deltaX;
	private final int deltaY;
	
	public TileMoveAction(TileRenderer tile, int deltaX, int deltaY) {
		this.tile = tile;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	@Override
	public void undo() {
		tile.moveBy(-deltaX, -deltaY);
	}
	
	@Override
	public void redo() {
		tile.moveBy(deltaX, deltaY);
	}
	
}
