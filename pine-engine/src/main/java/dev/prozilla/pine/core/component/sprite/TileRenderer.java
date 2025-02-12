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
	 */
	public void moveTo(int x, int y) {
		moveTo(new Vector2i(x, y));
	}
	
	/**
	 * Moves this tile to a coordinate.
	 * @param coordinate Coordinate
	 */
	public void moveTo(Vector2i coordinate) {
		if (this.coordinate.equals(coordinate)) {
			return;
		}
		
		GridGroup group = getGroup();
		
		if (group != null) {
			group.moveTile(this.coordinate, coordinate);
		} else {
			this.coordinate = coordinate;
		}
	}
	
	public boolean isHovering() {
		GridGroup group = getGroup();
		
		if (group == null) {
			return false;
		}
		
		return group.isHovering(this);
	}
	
	public void remove() {
		GridGroup group = getGroup();
		
		if (group != null) {
			group.removeTile(this);
		}
		
		entity.destroy();
	}
	
	public GridGroup getGroup() {
		if (group != null) {
			return group;
		}
		
		group = getComponentInParent(GridGroup.class, false);
		return group;
	}
}
