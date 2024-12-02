package dev.prozilla.pine.core.component;

import dev.prozilla.pine.core.entity.Entity;

import java.awt.*;
import java.util.ArrayList;
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
	public void init(long window) {
		super.init(window);
		
		if (!coordinateToTile.isEmpty()) {
			coordinateToTile.clear();
		}
		
		ArrayList<TileRenderer> tiles = entity.getComponentsInChildren(TileRenderer.class);
		
		if(tiles != null && !tiles.isEmpty()) {
			for (TileRenderer tile : tiles) {
				coordinateToTile.put(tile.coordinate, tile);
			}
		}
	}
	
	@Override
	public void input(float deltaTime) {
		super.input(deltaTime);
		
		float[] cursor = getInput().getWorldCursor();
		
		if (cursor != null && !coordinateToTile.isEmpty()) {
			int coordinateX = Math.round(cursor[0] / size - 0.5f);
			int coordinateY = Math.round(cursor[1] / size - 0.5f);
			Point coordinate = new Point(coordinateX, coordinateY);
			
			hoveringTile = getTile(coordinate);
			if (hoveringTile != null) {
				getInput().blockCursor(hoveringTile.getGameObject());
			}
		}
	}
	
	@Override
	public String getName() {
		return "GridGroup";
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
		return isHovering(tile.getGameObject());
	}
	
	public boolean isHovering(Entity entity) {
		if (hoveringTile == null || entity == null) {
			return false;
		}
		return entity.equals(hoveringTile.getGameObject());
	}
}
