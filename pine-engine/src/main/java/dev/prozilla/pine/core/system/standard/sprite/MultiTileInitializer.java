package dev.prozilla.pine.core.system.standard.sprite;

import dev.prozilla.pine.core.component.sprite.MultiTileRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public final class MultiTileInitializer extends InitSystem {
	
	public MultiTileInitializer() {
		super(TileRenderer.class, MultiTileRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		TileRenderer tile = chunk.getComponent(TileRenderer.class);
		MultiTileRenderer multiTile = chunk.getComponent(MultiTileRenderer.class);
		
		multiTile.init(tile);
	}
}
