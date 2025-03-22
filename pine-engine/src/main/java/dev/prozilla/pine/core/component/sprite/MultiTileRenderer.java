package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A component for rendering a multi-tile that spans across multiple tiles in a grid.
 */
public class MultiTileRenderer extends Component implements TileProvider {
	
	/** The x and y values represent the amount of tiles this multi-tile takes up */
	public Vector2i dimensions;
	
	public TileRenderer anchorTile;
	public List<PhantomTile> phantomTiles;
	
	public MultiTileRenderer(Vector2i dimensions) {
		this.dimensions = dimensions;
		phantomTiles = new ArrayList<>();
	}
	
	public void init() {
		GridGroup grid = anchorTile.getGroup();
		for (int x = 0; x < dimensions.x; x++) {
			for (int y = 0; y < dimensions.y; y++) {
				PhantomTile phantomTile = new PhantomTile(anchorTile, new Vector2i(x, y));
				phantomTiles.add(phantomTile);
				
				if (grid != null) {
					grid.coordinateToTile.put(phantomTile.getCoordinate(), phantomTile);
				}
			}
		}
	}
	
	@Override
	public void setCoordinate(Vector2i coordinate) {
		anchorTile.setCoordinate(coordinate);
		
		GridGroup grid = anchorTile.getGroup();
		if (grid != null) {
			for (PhantomTile phantomTile : phantomTiles) {
				// Remove entry for previous phantom tile coordinate and add entry for new coordinate
				grid.coordinateToTile.remove(phantomTile.coordinate);
				grid.coordinateToTile.put(phantomTile.getCoordinate(), phantomTile);
			}
		}
	}
	
	@Override
	public void remove() {
		anchorTile.remove();
		
		// Remove all phantom tiles as well
		GridGroup grid = anchorTile.getGroup();
		if (grid != null) {
			for (PhantomTile phantomTile : phantomTiles) {
				grid.coordinateToTile.remove(phantomTile.coordinate);
			}
		}
	}
	
	@Override
	public TileRenderer getTile() {
		return anchorTile;
	}
}
