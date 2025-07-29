package dev.prozilla.pine.core.system.standard.sprite;

import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.MultiTileRenderer;
import dev.prozilla.pine.core.component.sprite.PhantomTile;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

import java.util.List;

public final class GridInitializer extends InitSystem {
	
	public GridInitializer() {
		super(GridGroup.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Entity entity = chunk.getEntity();
		GridGroup gridGroup = chunk.getComponent(GridGroup.class);
		
		if (!gridGroup.coordinateToTile.isEmpty()) {
			gridGroup.coordinateToTile.clear();
		}
		
		List<TileRenderer> tiles = entity.getComponentsInChildren(TileRenderer.class);
		
		if (tiles != null) {
			for (TileRenderer tile : tiles) {
				gridGroup.coordinateToTile.put(tile.getCoordinate(), tile);
			}
		}
		
		List<MultiTileRenderer> multiTiles = entity.getComponentsInChildren(MultiTileRenderer.class);
		
		if (multiTiles != null) {
			for (MultiTileRenderer multiTile : multiTiles) {
				for (PhantomTile phantomTile : multiTile.phantomTiles) {
					gridGroup.coordinateToTile.put(phantomTile.getCoordinate(), phantomTile.getTile());
				}
			}
		}
	}
}
