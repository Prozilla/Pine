package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.GameManager;

import java.util.concurrent.atomic.AtomicInteger;

public class CrateUpdater extends UpdateSystemBase {
	
	private final GridGroup goalGrid;
	
	public CrateUpdater(GridGroup goalGrid) {
		super(TileRenderer.class, SpriteRenderer.class);
		setRequiredTag(EntityTag.CRATE);
		this.goalGrid = goalGrid;
	}
	
	@Override
	public void update(float deltaTime) {
		AtomicInteger completedCrates = new AtomicInteger();
		
		forEach((EntityChunk chunk) -> {
			TileRenderer tileRenderer = chunk.getComponent(TileRenderer.class);
			SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
			
			// Update sprite based on whether create is on a goal tile
			if (goalGrid.getTile(tileRenderer.getCoordinate()) != null) {
				spriteRenderer.texture = ResourcePool.loadTexture("images/crates/crate_07.png");
				completedCrates.addAndGet(1);
			} else {
				spriteRenderer.texture = ResourcePool.loadTexture("images/crates/crate_02.png");
			}
		});
		
		GameManager.instance.completedCrates = completedCrates.get();
	}

}
