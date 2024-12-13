package dev.prozilla.pine.core.system.standard.sprite;

import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

import java.awt.*;

public class GridInputHandler extends InputSystem {
	
	public GridInputHandler() {
		super(GridGroup.class);
	}
	
	@Override
	protected void process(EntityMatch match, Input input, float deltaTime) {
		GridGroup gridGroup = match.getComponent(GridGroup.class);
		
		float[] cursor = getInput().getWorldCursor();
		
		if (cursor != null && !gridGroup.coordinateToTile.isEmpty()) {
			int coordinateX = Math.round(cursor[0] / gridGroup.size - 0.5f);
			int coordinateY = Math.round(cursor[1] / gridGroup.size - 0.5f);
			Point coordinate = new Point(coordinateX, coordinateY);
			
			gridGroup.hoveringTile = gridGroup.getTile(coordinate);
			if (gridGroup.hoveringTile != null) {
				getInput().blockCursor(gridGroup.hoveringTile.getEntity());
			}
		}
	}
}
