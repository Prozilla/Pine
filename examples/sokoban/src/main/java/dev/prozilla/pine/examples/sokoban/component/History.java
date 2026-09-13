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
		getEntity().getComponent(PlayerData.class).teleportTo(grid, move.start(), move.direction());
		
		if (move.pushedCrate()) {
			TileRenderer crateTile = getCrate(grid, move.crateEnd());
			if (crateTile != null) {
				crateTile.moveTo(move.crateStart());
			}
		}
	}
	
	private boolean canUndo(GridGroup grid, Move move) {
		if (!isFreeOfPlayers(grid, move.start()) || isCrate(grid, move.start())) {
			return false;
		}
		
		if (!move.pushedCrate()) {
			return true;
		}
		
		TileRenderer crateTile = getCrate(grid, move.crateEnd());
		if (crateTile == null) {
			return false;
		}
		
		TileRenderer occupant = grid.getTile(move.crateStart());
		return occupant == null || occupant.getEntity() == getEntity();
	}
	
	private boolean isFreeOfPlayers(GridGroup grid, Vector2i coordinate) {
		TileRenderer tile = grid.getTile(coordinate);
		if (tile == null || !tile.getEntity().hasTag(EntityTag.PLAYER)) {
			return true;
		}
		return tile.getEntity() == getEntity();
	}
	
	private boolean isCrate(GridGroup grid, Vector2i coordinate) {
		return getCrate(grid, coordinate) != null;
	}
	
	private TileRenderer getCrate(GridGroup grid, Vector2i coordinate) {
		TileRenderer tile = grid.getTile(coordinate);
		return tile != null && tile.getEntity().hasTag(EntityTag.CRATE) ? tile : null;
	}
	
}
