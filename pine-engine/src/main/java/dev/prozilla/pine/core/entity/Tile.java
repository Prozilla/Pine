package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.common.system.resource.Texture;

import java.awt.*;

/**
 * Represents a 2D tile inside the world. Position is based on a Point.
 */
public class Tile extends Entity {
	
	protected GridGroup group;
	protected final TileRenderer tileRenderer;
	
	public Tile(World world, Texture texture) {
		this(world, texture, new Point(0, 0));
	}
	
	public Tile(World world, Texture texture, Point coordinate) {
		super(world);
		
		tileRenderer = new TileRenderer(texture, coordinate);
		addComponent(tileRenderer);
	}
	
	@Override
	public void setParent(Entity parent) {
		super.setParent(parent);
		
		if (parent == null) {
			group = null;
		} else {
			getGroup();
		}
	}
	
	@Override
	public String getName() {
		return getName("Tile");
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
		moveTo(Math.round(transform.x) + x, Math.round(transform.y + y));
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
