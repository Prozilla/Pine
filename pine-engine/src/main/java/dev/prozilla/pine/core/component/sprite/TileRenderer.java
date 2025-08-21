package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;


/**
 * A component for rendering 2D square tiles in the world.
 */
public class TileRenderer extends Component implements TileProvider {
	
	private Vector2i coordinate;
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
	
	@Override
	public void moveBy(Vector2i coordinate) {
		moveBy(coordinate.x, coordinate.y);
	}
	
	@Override
	public void moveBy(int x, int y) {
		moveTo(coordinate.x + x, coordinate.y + y);
	}
	
	@Override
	public boolean moveTo(int x, int y) {
		if (coordinate.x == x && coordinate.y == y) {
			return false;
		}
		
		return moveTo(new Vector2i(x, y));
	}
	
	@Override
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
	
	@Override
	public void setCoordinate(Vector2i coordinate) {
		this.coordinate = coordinate;
	}
	
	@Override
	public Vector2i getCoordinate() {
		return coordinate;
	}
	
	@Override
	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	public boolean isHovering() {
		GridGroup group = getGroup();
		
		if (group == null) {
			return false;
		}
		
		return group.isHovering(this);
	}
	
	@Override
	public void remove() {
		GridGroup group = getGroup();
		
		if (group != null) {
			group.removeTile(this);
		}
		
		entity.destroy();
	}
	
	@Override
	public GridGroup getGroup() {
		if (group != null) {
			return group;
		}
		
		group = getComponentInParent(GridGroup.class, false);
		return group;
	}
	
	@Override
	public TileRenderer getTile() {
		return this;
	}
	
	/**
	 * Returns the closest edge of this tile relative to a position in the world.
	 * @return The closest edge.
	 */
	public Direction getClosestEdge(Vector2f position) {
		return position != null ? getClosestEdge(position.x, position.y) : null;
	}
	
	/**
	 * Returns the closest edge of this tile relative to a position in the world.
	 * @param x The x position
	 * @param y The y position
	 * @return The closest edge.
	 */
	public Direction getClosestEdge(float x, float y) {
		float tileX = entity.transform.getGlobalX();
		float tileY = entity.transform.getGlobalY();
		
		float offsetX = x - (tileX + (size / 2f));
		float offsetY = y - (tileY + (size / 2f));
		
		return Direction.fromFloatVector(offsetX, offsetY);
	}
	
}
