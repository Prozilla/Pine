package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.examples.sokoban.EntityTag;

import java.util.ArrayDeque;
import java.util.Deque;

public class History extends Component {
	
	public final Deque<Move> undoStack = new ArrayDeque<>();
	
	public final Vector2i spawn;
	
	public History(Vector2i spawn) {
		this.spawn = spawn;
	}
	
	public void push(Move move) {
		undoStack.push(move);
	}
	
	public Move undo(GridGroup grid) {
		if (undoStack.isEmpty()) {
			return null;
		}
		
		Move move = undoStack.peek();
		if (!canUndo(grid, move)) {
			return null;
		}
		
		undoStack.pop();
		revert(move, grid);
		
		return move;
	}
	
	public void clear() {
		undoStack.clear();
	}
	
	public void revertPending(GridGroup grid) {
		PlayerData playerData = getEntity().getComponent(PlayerData.class);
		Move pending = playerData.pendingMove;
		
		if (pending == null) {
			return;
		}
		
		revert(pending, grid);
		playerData.pendingMove = null;
	}
	
	private void revert(Move move, GridGroup grid) {
		getEntity().getComponent(PlayerData.class).teleportTo(grid, move.fromX(), move.fromY(), move.direction());
		
		if (move.pushedCrate()) {
			TileRenderer crateTile = getCrate(grid, move.crateToX(), move.crateToY());
			if (crateTile != null) {
				crateTile.moveTo(new Vector2i(move.crateFromX(), move.crateFromY()));
			}
		}
	}
	
	private boolean canUndo(GridGroup grid, Move move) {
		int playerX = move.fromX();
		int playerY = move.fromY();
		if (!isFreeOfPlayers(grid, playerX, playerY) || isCrate(grid, playerX, playerY)) {
			return false;
		}
		
		if (!move.pushedCrate()) {
			return true;
		}
		
		TileRenderer crateTile = getCrate(grid, move.crateToX(), move.crateToY());
		if (crateTile == null) {
			return false;
		}
		
		TileRenderer occupant = grid.getTile(move.crateFromX(), move.crateFromY());
		return occupant == null || occupant.getEntity() == getEntity();
	}
	
	private boolean isFreeOfPlayers(GridGroup grid, int x, int y) {
		TileRenderer tile = grid.getTile(x, y);
		if (tile == null || !tile.getEntity().hasTag(EntityTag.PLAYER)) {
			return true;
		}
		return tile.getEntity() == getEntity();
	}
	
	private boolean isCrate(GridGroup grid, int x, int y) {
		return getCrate(grid, x, y) != null;
	}
	
	private TileRenderer getCrate(GridGroup grid, int x, int y) {
		TileRenderer tile = grid.getTile(x, y);
		return tile != null && tile.getEntity().hasTag(EntityTag.CRATE) ? tile : null;
	}
	
}
