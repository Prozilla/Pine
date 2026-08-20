package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.common.math.vector.Direction;

public record Move(
	int playerId,
	Direction direction,
	int fromX,
	int fromY,
	int toX,
	int toY,
	boolean pushedCrate,
	int crateFromX,
	int crateFromY,
	int crateToX,
	int crateToY
) {
	
}
