package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.state.input.Input;
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
		Input input = getInput();
		Point cursor = input.getCursor();
		
		forEachReverse(match -> {
			Entity entity = match.getEntity();
			RectTransform rect = match.getComponent(RectTransform.class);
			
			rect.cursorHit = false;
			
			if (!rect.passThrough && !input.isCursorBlocked()) {
				int canvasHeight = rect.getCanvas().getHeight();
				if (cursor != null && rect.isInside(cursor.x, canvasHeight - cursor.y, rect.x, rect.y, rect.width, rect.height)) {
					rect.cursorHit = true;
					input.blockCursor(entity);
				}
			}
		});
	}
}
