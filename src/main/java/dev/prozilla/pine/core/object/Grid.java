package dev.prozilla.pine.core.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.GridGroup;
import dev.prozilla.pine.core.component.TileRenderer;

import java.awt.*;

public class Grid extends GameObject {
	
	protected final GridGroup group;
	
	public Grid(Game game, int size) {
		this(game, "Grid", size);
	}
	
	public Grid(Game game, String name, int size) {
		super(game, name);
		
		group = new GridGroup(size);
		addComponent(group);
	}
	
	@Override
	public GameObject addChild(GameObject child) throws IllegalStateException, IllegalArgumentException {
		super.addChild(child);
		group.addTile(child);
		return child;
	}
	
	@Override
	public void removeChild(GameObject child) throws IllegalStateException, IllegalArgumentException {
		super.removeChild(child);
		group.removeTile(child);
	}
	
	public Tile addTile(Tile tile) {
		return (Tile)addTile(tile.getTileRenderer());
	}
	
	public GameObject addTile(TileRenderer tile) {
		if (tile == null) {
			return null;
		}
		if (getTile(tile.coordinate) != null) {
			throw new IllegalStateException("Tiles cannot be placed on the same coordinates"
				+  System.lineSeparator() + "Use multiple grids to overlap tiles");
		}
		
		super.addChild(tile.getGameObject());
		group.addTile(tile);
		return tile.getGameObject();
	}
	
	public void removeTile(Tile tile) {
		removeTile(tile.getTileRenderer());
	}
	
	public void removeTile(TileRenderer tile) {
		if (tile == null) {
			return;
		}
		super.removeChild(tile.getGameObject());
		group.removeTile(tile);
	}
	
	public TileRenderer getTile(Point coordinate) {
		return group.getTile(coordinate);
	}
}
