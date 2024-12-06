package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.system.input.InputSystemBase;

import java.awt.*;

/**
 * Handles clicks on canvas elements.
 */
public class RectInputHandler extends InputSystemBase {
	
	public RectInputHandler() {
		super(RectTransform.class);
	}
	
	@Override
	public void input(float deltaTime) {
		Point cursor = getInput().getCursor();
		
		forEach(match -> {
			Entity entity = match.getEntity();
			RectTransform rect = match.getComponent(RectTransform.class);
			
			if (!rect.passThrough) {
				int canvasHeight = rect.getCanvas().getHeight();
				if (cursor != null && rect.isInside(cursor.x, canvasHeight - cursor.y, rect.x, rect.y, rect.width, rect.height)) {
					getInput().blockCursor(entity);
				}
			}
		});
	}
}
