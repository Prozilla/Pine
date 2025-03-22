package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Vector2i;

/**
 * Represents a tile in a grid that is part of a multi-tile.
 * The phantom tile delegates its operations to the anchor tile of the multi-tile.
 */
public class PhantomTile implements TileProvider {
	
	private final TileRenderer anchorTile;
	public Vector2i coordinate;
	public final Vector2i offset;
	
	public PhantomTile(TileRenderer anchorTile, Vector2i offset) {
		this.anchorTile = anchorTile;
		this.offset = offset;
	}
	
	@Override
	public Vector2i getCoordinate() {
		coordinate.x = anchorTile.getCoordinate().x + offset.x;
		coordinate.y = anchorTile.getCoordinate().y + offset.y;
		
		return coordinate;
	}
	
	@Override
	public TileRenderer getTile() {
		return anchorTile;
	}
}
