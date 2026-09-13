package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.mesh.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.GameMap;

import java.util.Map;

public class PlayerData extends Component {
	
	public Direction direction;
	public boolean canMove;
	public float timeUntilMoveCompletes;
	public Move pendingMove;
	public boolean awaitingConfirm;
	
	public SpriteRenderer pushingCrateSprite;
	public TileRenderer pushingCrateTile;
	
	public final int index;
	
	public static final Map<Direction, String[]> directionToSprites = Map.of(
		Direction.DOWN, new String[] {
			"images/player/player_23.png",
			"images/player/player_01.png",
			"images/player/player_24.png"
		},
		Direction.UP, new String[] {
			"images/player/player_02.png",
			"images/player/player_04.png",
			"images/player/player_03.png"
		},
		Direction.LEFT, new String[] {
			"images/player/player_14.png",
			"images/player/player_15.png",
			"images/player/player_16.png"
		},
		Direction.RIGHT, new String[] {
			"images/player/player_11.png",
			"images/player/player_13.png",
			"images/player/player_12.png"
		}
	);
	
	public static final float TIME_TO_MOVE = 0.25f;
	public static final float JOYSTICK_THRESHOLD = 0.5f;
	
	public PlayerData(int index) {
		this.index = index;
		timeUntilMoveCompletes = 0;
		canMove = false;
	}
	
	public Move computeMove(GridGroup grid, Direction direction) {
		TileRenderer tileRenderer = getEntity().getComponent(TileRenderer.class);
		Vector2i start = tileRenderer.getCoordinate();
		Vector2i end = direction.toIntVector().add(start);
		
		if (!GameMap.contains(end)) {
			return null;
		}
		
		TileRenderer targetTile = grid.getTile(end);
		boolean pushedCrate = false;
		Vector2i crateStart = null;
		Vector2i crateEnd = null;
		
		if (targetTile != null) {
			if (!targetTile.getEntity().hasTag(EntityTag.CRATE)) {
				return null;
			}
			
			crateStart = end.clone();
			crateEnd = direction.toIntVector().add(crateStart);
			
			if (!GameMap.contains(crateEnd) || grid.hasTile(crateEnd)) {
				return null;
			}
			
			pushedCrate = true;
		}
		
		return new Move(index, direction, start, end, pushedCrate, crateStart, crateEnd);
	}
	
	public void beginMove(Move move, GridGroup grid) {
		startMove(move.direction());
		canMove = true;
		timeUntilMoveCompletes = TIME_TO_MOVE;
		pendingMove = move;
		
		if (move.pushedCrate()) {
			TileRenderer crateTile = grid.getTile(move.crateStart());
			if (crateTile != null && crateTile.getEntity().hasTag(EntityTag.CRATE)) {
				pushingCrateTile = crateTile;
				pushingCrateSprite = crateTile.getComponent(SpriteRenderer.class);
				getEntity().getComponent(AudioEffectPlayer.class).play(0);
			}
		}
	}
	
	public void finishMove() {
		TileRenderer tileRenderer = getEntity().getComponent(TileRenderer.class);
		SpriteRenderer spriteRenderer = getEntity().getComponent(SpriteRenderer.class);
		
		spriteRenderer.getMesh().setOffset(0, 0);
		
		if (pushingCrateSprite != null) {
			pushingCrateSprite.getMesh().setOffset(0, 0);
		}
		
		if (pendingMove != null) {
			if (pendingMove.pushedCrate() && pushingCrateTile != null) {
				pushingCrateTile.moveTo(new Vector2i(pendingMove.crateEnd()));
			}
			
			tileRenderer.moveTo(new Vector2i(pendingMove.end()));
		}
		
		startMove(null);
		canMove = false;
		timeUntilMoveCompletes = 0;
		pendingMove = null;
	}
	
	public void blockMove(Direction direction) {
		startMove(direction);
		canMove = false;
		timeUntilMoveCompletes = 0;
	}
	
	public void teleportTo(GridGroup grid, Vector2i destination) {
		teleportTo(grid, destination, null);
	}
	
	public void teleportTo(GridGroup grid, Vector2i destination, Direction facing) {
		startMove(facing);
		canMove = false;
		timeUntilMoveCompletes = 0;
		pendingMove = null;
		
		SpriteRenderer spriteRenderer = getEntity().getComponent(SpriteRenderer.class);
		spriteRenderer.getMesh().setOffset(0, 0);
		
		TileRenderer tileRenderer = getEntity().getComponent(TileRenderer.class);
		
		Vector2i coordinate = tileRenderer.getCoordinate();
		TileRenderer occupant = grid.getTile(coordinate);
		if (occupant != null && occupant.getEntity() == getEntity()) {
			grid.removeTile(tileRenderer);
		}
		
		tileRenderer.setCoordinate(destination);
		grid.addTile(tileRenderer);
	}
	
	private void startMove(Direction direction) {
		this.direction = direction;
		
		pushingCrateTile = null;
		pushingCrateSprite = null;
	}
	
}
