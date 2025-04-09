package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.examples.sokoban.EntityTag;

import java.util.Map;

public class PlayerData extends Component {
	
	public Direction direction;
	public boolean canMove;
	public float timeUntilMoveCompletes;
	
	public SpriteRenderer pushingCrateSprite;
	public TileRenderer pushingCrateTile;
	
	public static final Map<Direction, String[]> directionToSprites = Map.of(
		Direction.DOWN, new String[]{
			"sokoban/PNG/Default size/Player/player_23.png",
			"sokoban/PNG/Default size/Player/player_01.png",
			"sokoban/PNG/Default size/Player/player_24.png"
		},
		Direction.UP, new String[]{
			"sokoban/PNG/Default size/Player/player_02.png",
			"sokoban/PNG/Default size/Player/player_04.png",
			"sokoban/PNG/Default size/Player/player_03.png"
		},
		Direction.LEFT, new String[]{
			"sokoban/PNG/Default size/Player/player_14.png",
			"sokoban/PNG/Default size/Player/player_15.png",
			"sokoban/PNG/Default size/Player/player_16.png"
		},
		Direction.RIGHT, new String[]{
			"sokoban/PNG/Default size/Player/player_11.png",
			"sokoban/PNG/Default size/Player/player_13.png",
			"sokoban/PNG/Default size/Player/player_12.png"
		}
	);
	
	public static final float TIME_TO_MOVE = 0.25f;
	
	public PlayerData() {
		timeUntilMoveCompletes = 0;
		canMove = false;
	}
	
	public void moveInDirection(Direction direction, TileRenderer tileRenderer) {
		if (timeUntilMoveCompletes > 0) {
			return;
		}
		
		this.direction = direction;
		
		pushingCrateTile = null;
		pushingCrateSprite = null;
		
		GridGroup gridGroup = tileRenderer.getGroup();
		Vector2i targetCoordinate = direction.toIntVector().add(tileRenderer.getCoordinate());
		TileRenderer targetTile = gridGroup != null ? gridGroup.getTile(targetCoordinate) : null;
		
		// Check if player can move to an empty tile or push a crate
		if (targetTile == null) {
			canMove = true;
		} else if (!targetTile.getEntity().hasTag(EntityTag.BLOCK)) {
			Vector2i behindTargetCoordinate = direction.toIntVector().add(targetCoordinate);
			TileRenderer behindTargetTile = gridGroup.getTile(behindTargetCoordinate);
			
			// Player can only push one crate at a time
			canMove = behindTargetTile == null || behindTargetTile.getEntity().hasTag(EntityTag.GOAL);
			
			if (canMove && targetTile.getEntity().hasTag(EntityTag.CRATE)) {
				pushingCrateTile = targetTile;
				pushingCrateSprite = targetTile.getComponent(SpriteRenderer.class);
			}
		} else {
			canMove = false;
		}
		
		// Reset timer
		timeUntilMoveCompletes = canMove ? TIME_TO_MOVE : 0;
	}
	
}
