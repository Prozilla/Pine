package dev.prozilla.pine.examples.sokoban.component;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;

public record Move(
	int playerId,
	Direction direction,
	Vector2i start,
	Vector2i end,
	boolean pushedCrate,
	Vector2i crateStart,
	Vector2i crateEnd
) {}
