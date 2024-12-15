package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A component that groups 2D tiles together and aligns them in a grid pattern.
 */
public class GridGroup extends Component {
	
	public int size;
	public final Map<Point, TileRenderer> coordinateToTile;
	public TileRenderer hoveringTile;
	
	public GridGroup(int size) {
		this.size = size;
		
		coordinateToTile = new HashMap<>();
	}
	
	@Override
	public String getName() {
		return "GridGroup";
	}
	
	public TileRenderer addTile(TilePrefab tilePrefab, Point coordinate) {
		return addTile(tilePrefab.instantiate(entity.getWorld(), coordinate));
	}
	
	public TileRenderer addTile(Prefab prefab) {
		return addTile(prefab.instantiate(entity.getWorld()));
	}
	
	public TileRenderer addTile(Entity entity) {
		return addTile(entity.getComponent(TileRenderer.class));
	}
	
	public TileRenderer addTile(TileRenderer tile) {
		if (tile == null || coordinateToTile.containsKey(tile.coordinate)) {
			return null;
		}
		
		tile.size = size;
		coordinateToTile.put(tile.coordinate, tile);
		
		if (!entity.transform.children.contains(tile.getEntity().transform)) {
			entity.addChild(tile.getEntity());
		}
		
		return tile;
	}
	
	public boolean removeTile(Point coordinate) {
		return removeTile(getTile(coordinate));
	}
	
	public boolean removeTile(Entity entity) {
		return removeTile(entity.getComponent(TileRenderer.class));
	}
	
	public boolean removeTile(TileRenderer tile) {
		if (tile == null || !coordinateToTile.containsKey(tile.coordinate)) {
			return false;
		}
		
		if (entity.transform.children.contains(tile.getEntity().transform)) {
			entity.removeChild(tile.getEntity());
		}
		
		return (coordinateToTile.remove(tile.coordinate) != null);
	}
	
	public TileRenderer getTile(Point coordinate) {
		return coordinateToTile.get(coordinate);
	}
	
	public void moveTile(Point oldCoordinate, Point newCoordinate) {
		TileRenderer tile = getTile(oldCoordinate);
		if (removeTile(tile)) {
			tile.coordinate = newCoordinate;
			addTile(tile);
		}
	}
	
	public boolean isHovering(TileRenderer tile) {
		return isHovering(tile.getEntity());
	}
	
	public boolean isHovering(Entity entity) {
		if (hoveringTile == null || entity == null) {
			return false;
		}
		return entity.equals(hoveringTile.getEntity());
	}
}
