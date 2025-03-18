package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;


/**
 * A component for rendering 2D square tiles in the world.
 */
public class TileRenderer extends Component {
	
	public Vector2i coordinate;
	/** Width and height of the tile, in pixels. */
	public int size;
	public GridGroup group;
	
	public TileRenderer(Vector2i coordinate, int size) {
		this.coordinate = coordinate;
		this.size = size;
	}
	
	@Override
	public String getName() {
		return "TileRenderer";
	}
	
	/**
	 * Moves this tile by an x and y amount based on a coordinate
	 * @param coordinate Coordinate
	 */
	public void moveBy(Vector2i coordinate) {
		moveBy(coordinate.x, coordinate.y);
	}
	
	/**
	 * Moves this tile by an x and y amount.
	 * @param x X value
	 * @param y Y value
	 */
	public void moveBy(int x, int y) {
		moveTo(coordinate.x + x, coordinate.y + y);
	}
	
	/**
	 * Moves this tile to an XY-coordinate.
	 * @param x X value
	 * @param y Y value
	 * @return True if the coordinate of this tile was changed.
	 */
	public boolean moveTo(int x, int y) {
		if (coordinate.x == x && coordinate.y == y) {
			return false;
		}
		
		return moveTo(new Vector2i(x, y));
	}
	
	/**
	 * Moves this tile to a coordinate.
	 * @param coordinate Coordinate
	 * @return True if the coordinate of this tile was changed.
	 */
	public boolean moveTo(Vector2i coordinate) {
		if (this.coordinate.equals(coordinate)) {
			return false;
		}
		
		GridGroup group = getGroup();
		
		if (group != null) {
			group.moveTile(this.coordinate, coordinate);
		} else {
			this.coordinate = coordinate;
		}
		
		return true;
	}
	
	public boolean isHovering() {
		GridGroup group = getGroup();
		
		if (group == null) {
			return false;
		}
		
		return group.isHovering(this);
	}
	
	/**
	 * Removes this tile from its grid.
	 */
	public void remove() {
		GridGroup group = getGroup();
		
		if (group != null) {
			group.removeTile(this);
		}
		
		entity.destroy();
	}
	
	/**
	 * @return The grid this tile belongs to.
	 */
	public GridGroup getGroup() {
		if (group != null) {
			return group;
		}
		
		group = getComponentInParent(GridGroup.class, false);
		return group;
	}
}
