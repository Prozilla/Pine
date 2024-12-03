package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.GridGroup;
import dev.prozilla.pine.core.component.TileRenderer;

import java.awt.*;

public class Grid extends Entity {
	
	protected final GridGroup group;
	
	public Grid(World world, int size) {
		super(world);
		
		group = new GridGroup(size);
		addComponent(group);
	}
	
	@Override
	public Entity addChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		super.addChild(child);
		group.addTile(child);
		return child;
	}
	
	@Override
	public void removeChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		super.removeChild(child);
		group.removeTile(child);
	}
	
	@Override
	public String getName() {
		return getName("Grid");
	}
	
	public Tile addTile(Tile tile) {
		return (Tile)addTile(tile.getTileRenderer());
	}
	
	public Entity addTile(TileRenderer tile) {
		if (tile == null) {
			return null;
		}
		if (getTile(tile.coordinate) != null) {
			throw new IllegalStateException("Tiles cannot be placed on the same coordinates"
				+  System.lineSeparator() + "Use multiple grids to overlap tiles");
		}
		
		super.addChild(tile.getEntity());
		group.addTile(tile);
		return tile.getEntity();
	}
	
	public void removeTile(Tile tile) {
		removeTile(tile.getTileRenderer());
	}
	
	public void removeTile(TileRenderer tile) {
		if (tile == null) {
			return;
		}
		super.removeChild(tile.getEntity());
		group.removeTile(tile);
	}
	
	public TileRenderer getTile(Point coordinate) {
		return group.getTile(coordinate);
	}
}
