package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
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
		
		forEachReverse(chunk -> {
			Entity entity = chunk.getEntity();
			RectTransform rect = chunk.getComponent(RectTransform.class);
			
			rect.cursorHit = false;
			
			if (!rect.passThrough && !input.isCursorBlocked()) {
				int canvasHeight = rect.getCanvas().getHeight();
				if (cursor != null && RectTransform.isInsideRect(new Vector2i(cursor.x, canvasHeight - cursor.y), rect.position, rect.size)) {
					rect.cursorHit = true;
					input.blockCursor(entity);
				}
			}
		});
	}
}
