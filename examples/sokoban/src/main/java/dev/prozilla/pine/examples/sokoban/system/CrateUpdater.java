package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.sokoban.EntityTag;

public class CrateUpdater extends UpdateSystem {
	
	private final GridGroup goalGrid;
	
	public CrateUpdater(GridGroup goalGrid) {
		super(TileRenderer.class, SpriteRenderer.class);
		setRequiredTag(EntityTag.CRATE);
		this.goalGrid = goalGrid;
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		TileRenderer tileRenderer = chunk.getComponent(TileRenderer.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		
		// Update sprite based on whether create is on a goal tile
		if (goalGrid.getTile(tileRenderer.getCoordinate()) != null) {
			spriteRenderer.texture = ResourcePool.loadTexture("sokoban/Crates/crate_07.png");
		} else {
			spriteRenderer.texture = ResourcePool.loadTexture("sokoban/Crates/crate_02.png");
		}
	}
}
