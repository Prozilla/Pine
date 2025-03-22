package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityProvider;

/**
 * Provides access to a {@link TileRenderer} component.
 */
public interface TileProvider extends EntityProvider {
	
	/**
	 * Moves this tile by an x and y amount based on a coordinate
	 * @param coordinate Coordinate
	 */
	default void moveBy(Vector2i coordinate) {
		getTile().moveBy(coordinate);
	}
	
	/**
	 * Moves this tile by an x and y amount.
	 * @param x X value
	 * @param y Y value
	 */
	default void moveBy(int x, int y) {
		getTile().moveBy(x, y);
	}
	
	/**
	 * Moves this tile to an XY-coordinate.
	 * @param x X value
	 * @param y Y value
	 * @return True if the coordinate of this tile was changed.
	 */
	default boolean moveTo(int x, int y) {
		return getTile().moveTo(x, y);
	}
	
	/**
	 * Moves this tile to a coordinate.
	 * @param coordinate Coordinate
	 * @return True if the coordinate of this tile was changed.
	 */
	default boolean moveTo(Vector2i coordinate) {
		return getTile().moveTo(coordinate);
	}
	
	default void setCoordinate(Vector2i coordinate) {
		getTile().setCoordinate(coordinate);
	}
	
	default Vector2i getCoordinate() {
		return getTile().getCoordinate();
	}
	
	default void setSize(int size) {
		getTile().size = size;
	}
	
	default boolean isHovering() {
		return getTile().isHovering();
	}
	
	/**
	 * Removes this tile from its grid.
	 */
	default void remove() {
		getTile().remove();
	}
	
	default Transform getTransform() {
		return getEntity().transform;
	}
	
	default Entity getEntity() {
		return getTile().getEntity();
	}
	
	/**
	 * @return The grid this tile belongs to, or <code>null</code> if this tile is not in a grid.
	 */
	default GridGroup getGroup() {
		return getTile().getGroup();
	}
	
	TileRenderer getTile();
	
}
