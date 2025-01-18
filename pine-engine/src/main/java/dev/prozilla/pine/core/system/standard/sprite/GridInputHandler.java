package dev.prozilla.pine.core.system.standard.sprite;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

public class GridInputHandler extends InputSystem {
	
	public GridInputHandler() {
		super(GridGroup.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		GridGroup gridGroup = chunk.getComponent(GridGroup.class);
		Vector2f cursor = input.getWorldCursor();
		
		if (cursor != null && !gridGroup.coordinateToTile.isEmpty()) {
			// Calculate grid coordinate based on cursor position
			int coordinateX = Math.round(cursor.x / gridGroup.size - 0.5f);
			int coordinateY = Math.round(cursor.y / gridGroup.size - 0.5f);
			Vector2i coordinate = new Vector2i(coordinateX, coordinateY);
			
			// Check which tile the cursor is hovering
			gridGroup.hoveringTile = gridGroup.getTile(coordinate);
			if (gridGroup.hoveringTile != null) {
				input.blockCursor(gridGroup.hoveringTile.getEntity());
			}
		}
	}
}
