package dev.prozilla.pine.core.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.GridGroup;
import dev.prozilla.pine.core.component.TileRenderer;
import dev.prozilla.pine.common.system.resource.Texture;

import java.awt.*;

/**
 * Represents a 2D tile inside the world. Position is based on a Point.
 */
public class Tile extends GameObject {
	
	protected GridGroup group;
	protected final TileRenderer tileRenderer;
	
	public Tile(Game game, Texture texture) {
		this(game, "Tile", texture);
	}
	
	public Tile(Game game, String name, Texture texture) {
		this(game, name, texture, new Point(0, 0));
	}
	
	public Tile(Game game, String name, Texture texture, Point coordinate) {
		super(game, name);
		
		tileRenderer = new TileRenderer(texture, coordinate);
		addComponent(tileRenderer);
	}
	
	@Override
	public void setParent(GameObject parent) {
		super.setParent(parent);
		
		if (parent == null) {
			group = null;
		} else {
			getGroup();
		}
	}
	
	/**
	 * Moves this tile by an x and y amount based on a coordinate
	 * @param coordinate Coordinate
	 */
	public void moveBy(Point coordinate) {
		moveBy(coordinate.x, coordinate.y);
	}
	
	/**
	 * Moves this tile by an x and y amount.
	 * @param x X value
	 * @param y Y value
	 */
	public void moveBy(int x, int y) {
		moveTo(this.x + x, this.y + y);
	}
	
	/**
	 * Moves this tile to an XY-coordinate.
	 * @param x X value
	 * @param y Y value
	 */
	public void moveTo(int x, int y) {
		moveTo(new Point(x, y));
	}
	
	/**
	 * Moves this tile to a coordinate.
	 * @param coordinate Coordinate
	 */
	public void moveTo(Point coordinate) {
		GridGroup group = getGroup();
		
		if (group != null) {
			group.moveTile(tileRenderer.coordinate, coordinate);
		} else {
			tileRenderer.coordinate = coordinate;
		}
	}
	
	public boolean isHovering() {
		GridGroup group = getGroup();

		if (group == null) {
			return false;
		}

		return group.isHovering(this);
	}
	
	public GridGroup getGroup() {
		if (group != null) {
			return group;
		}
		
		group = getComponentInParent(GridGroup.class, false);
		return group;
	}
	
	public TileRenderer getTileRenderer() {
		return tileRenderer;
	}
	
	public Point getCoordinate() {
		return tileRenderer.coordinate;
	}
}
