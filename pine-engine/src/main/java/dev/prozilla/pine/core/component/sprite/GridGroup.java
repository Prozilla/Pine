package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.core.system.standard.sprite.TileMover;

import java.util.HashMap;
import java.util.Map;

/**
 * A component that groups 2D tiles together and aligns them in a grid pattern.
 */
public class GridGroup extends Component {
	
	public int size;
	public final Map<Vector2i, TileProvider> coordinateToTile;
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
		Checks.isNotNull(tile, "tile");
		
		if (coordinateToTile.containsKey(tile.getCoordinate())) {
			throw new IllegalStateException("multiple tiles cannot be placed on the same coordinate in one grid");
		}
		
		tile.setSize(size);
		coordinateToTile.put(tile.getCoordinate(), tile);
		TileMover.updateTilePosition(tile.getTransform(), tile.getTile());
		
		MultiTileRenderer multiTile = tile.getComponent(MultiTileRenderer.class);
		if (multiTile != null) {
			for (PhantomTile phantomTile : multiTile.phantomTiles) {
				coordinateToTile.put(phantomTile.getCoordinate(), tile);
			}
		}
		
		if (!entity.transform.children.contains(tile.getEntity().transform)) {
			entity.addChild(tile.getEntity());
		}
		
		tile.getEntity().addListener(Entity.EventType.DESTROY, (entity) -> coordinateToTile.remove(tile.getCoordinate()));
		
		return tile.getTile();
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
		if (tile == null || !coordinateToTile.containsKey(tile.getCoordinate())) {
			return false;
		}
		
		if (entity.transform.children.contains(tile.getEntity().transform)) {
			entity.removeChild(tile.getEntity());
		}
		
		boolean removed = (coordinateToTile.remove(tile.getCoordinate()) != null);
		
		if (removed) {
			MultiTileRenderer multiTile = tile.getComponent(MultiTileRenderer.class);
			if (multiTile != null) {
				for (PhantomTile phantomTile : multiTile.phantomTiles) {
					coordinateToTile.remove(phantomTile.getCoordinate());
				}
			}
		}
		
		return removed;
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
		TileProvider tileProvider = coordinateToTile.get(coordinate);
		if (tileProvider == null) {
			return null;
		}
		return tileProvider.getTile();
	}
	
	public void moveTile(Vector2i oldCoordinate, Vector2i newCoordinate) {
		TileRenderer tile = getTile(oldCoordinate);
		if (removeTile(tile)) {
			tile.setCoordinate(newCoordinate);
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
	
	/**
	 * Returns an array of coordinates of a subgrid defined by the given dimensions, with a given offset.
	 * @param dimensions The dimensions of the subgrid
	 * @param offset The offset of the subgrid
	 * @return The coordinates of the subgrid
	 * @see #subgrid(Vector2i)
	 */
	public static Vector2i[] subgrid(Vector2i dimensions, Vector2i offset) {
		Vector2i[] coordinates = subgrid(dimensions);
		for (Vector2i coordinate : coordinates) {
			coordinate.add(offset);
		}
		return coordinates;
	}
	
	/**
	 * Returns an array of coordinates of a subgrid defined by the given dimensions.
	 * @param dimensions The dimensions of the subgrid
	 * @return The coordinates of the subgrid
	 * @see #subgrid(int, int)
	 */
	public static Vector2i[] subgrid(Vector2i dimensions) {
		return subgrid(dimensions.x, dimensions.y);
	}
	
	/**
	 * Returns an array of coordinates of a subgrid defined by the given dimensions.
	 *
	 * <p>The x-coordinates go from {@code 0} to {@code width - 1} and the y-coordinates go from {@code 0} to {@code height - 1}.</p>
	 *
	 * <p>The array is sorted first based on the x-coordinate, then the y-coordinate.</p>
	 * @param width The width of the subgrid
	 * @param height The height of the subgrid
	 * @return The coordinates of the subgrid
	 */
	public static Vector2i[] subgrid(int width, int height) {
		Vector2i[] coordinates = new Vector2i[width * height];
		
		int i = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				coordinates[i] = new Vector2i(x, y);
				i++;
			}
		}
		
		return coordinates;
	}
	
}
