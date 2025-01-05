package dev.prozilla.pine.core.system.standard.sprite;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Updates the position of tiles based on their coordinate.
 */
public class TileMover extends UpdateSystem {
	
	public TileMover() {
		super( Transform.class, TileRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getComponent(Transform.class);
		TileRenderer tileRenderer = chunk.getComponent(TileRenderer.class);
		
		updateTilePosition(transform, tileRenderer);
	}
	
	public static void updateTilePosition(Transform transform, TileRenderer tileRenderer) {
		float x = tileRenderer.coordinate.x * tileRenderer.size;
		float y = tileRenderer.coordinate.y * tileRenderer.size;
		
		transform.setPosition(x, y);
	}
}
