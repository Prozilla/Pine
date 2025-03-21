package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityEvent;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.core.system.standard.sprite.TileMover;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A component that groups 2D tiles together and aligns them in a grid pattern.
 */
public class GridGroup extends Component {
	
	public int size;
	public final Map<Vector2i, TileRenderer> coordinateToTile;
	public TileRenderer hoveringTile;
	
	public GridGroup(int size) {
		this.size = size;
		
		coordinateToTile = new HashMap<>();
	}
	
	@Override
	public String getName() {
		return "GridGroup";
	}
	
	public TileRenderer addTile(TilePrefab tilePrefab, Vector2i coordinate) {
		return addTile(tilePrefab.instantiate(entity.getWorld(), coordinate));
	}
	
	public TileRenderer addTile(TilePrefab tilePrefab, int x, int y) {
		return addTile(tilePrefab.instantiate(entity.getWorld(), x, y));
	}
	
	public TileRenderer addTile(Prefab prefab) {
		return addTile(prefab.instantiate(entity.getWorld()));
	}
	
	public TileRenderer addTile(Entity entity) {
		return addTile(entity.getComponent(TileRenderer.class));
	}
	
	/**
	 * Adds a tile to this grid based on the tile's current coordinate.
	 * @param tile Tile to add to this grid
	 * @throws IllegalStateException If there is already a tile in this grid with the same coordinate
	 */
	public TileRenderer addTile(TileRenderer tile) throws NullPointerException, IllegalStateException {
		Objects.requireNonNull(tile, "tile must not be null");
		
		if (coordinateToTile.containsKey(tile.coordinate)) {
			throw new IllegalStateException("multiple tiles cannot be placed on the same coordinate in one grid");
		}
		
		tile.size = size;
		coordinateToTile.put(tile.coordinate, tile);
		TileMover.updateTilePosition(tile.getTransform(), tile);
		
		if (!entity.transform.children.contains(tile.getEntity().transform)) {
			entity.addChild(tile.getEntity());
		}
		
		tile.getEntity().addListener(EntityEvent.DESTROY, () -> {
			coordinateToTile.remove(tile.coordinate);
		});
		
		return tile;
	}
	
	public boolean destroyTile(Vector2i coordinate) {
		return destroyTile(getTile(coordinate));
	}
	
	public boolean destroyTile(Entity entity) {
		return destroyTile(entity.getComponent(TileRenderer.class));
	}
	
	public boolean destroyTile(TileRenderer tile) {
		boolean removed = removeTile(tile);
		tile.getEntity().destroy();
		return removed;
	}
	
	public boolean removeTile(Vector2i coordinate) {
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
	
	public boolean hasTile(int x, int y) {
		return hasTile(new Vector2i(x, y));
	}
	
	public boolean hasTile(Vector2i coordinate) {
		return coordinateToTile.containsKey(coordinate);
	}
	
	public TileRenderer getTile(int x, int y) {
		return getTile(new Vector2i(x, y));
	}
	
	public TileRenderer getTile(Vector2i coordinate) {
		return coordinateToTile.get(coordinate);
	}
	
	public void moveTile(Vector2i oldCoordinate, Vector2i newCoordinate) {
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
		
		Entity hoveringEntity = hoveringTile.getEntity();
		
		if (hoveringEntity == null) {
			return false;
		}
		
		return entity.equals(hoveringEntity);
	}
	
	public Vector2i positionToCoordinate(Vector2f position) {
		return positionToCoordinate(position.x, position.y);
	}
	
	public Vector2i positionToCoordinate(float x, float y) {
		return new Vector2i((int)Math.floor(x / (float)size), (int)Math.floor(y / (float)size));
	}
	
	public Vector2f coordinateToPosition(Vector2i coordinate) {
		return coordinateToPosition(coordinate.x, coordinate.y);
	}
	
	public Vector2f coordinateToPosition(int x, int y) {
		return new Vector2f(x * size, y * size);
	}
}
